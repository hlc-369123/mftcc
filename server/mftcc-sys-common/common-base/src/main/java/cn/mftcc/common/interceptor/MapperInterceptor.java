/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.interceptor;

import cn.mftcc.common.annotation.MftccDataAuthority;
import cn.mftcc.common.utils.AuthorityUtil;
import cn.mftcc.common.utils.RequestUtil;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisDefaultParameterHandler;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName MapperInterceptor
 * @Description Mybatis拦截器
 * @Author 郭涵晨
 * @Date 2020/3/3 11:20
 */

@Component

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
public class MapperInterceptor extends AbstractSqlParserHandler implements Interceptor {

    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private AuthorityUtil authorityUtil;

    private ISqlParser sqlParser;
    private boolean overflow = false;
    private String dialectType;
    private String dialectClazz;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        //先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
//        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.parameterHandler");
        //daoPath，如com.uv.dao.UserMapper.insertUser
        String daoPath = mappedStatement.getId();
        //sql语句类型 select、delete、insert、update
        String sqlCommandType = mappedStatement.getSqlCommandType().toString();
        if(!"SELECT".equals(sqlCommandType)){
            return invocation.proceed();
        }

        BoundSql boundSql = statementHandler.getBoundSql();

        Object paramObj = boundSql.getParameterObject();
        IPage page = null;
        if (paramObj instanceof IPage) {
            page = (IPage)paramObj;
        } else if (paramObj instanceof Map) {
            Iterator var8 = ((Map)paramObj).values().iterator();

            while(var8.hasNext()) {
                Object arg = var8.next();
                if (arg instanceof IPage) {
                    page = (IPage)arg;
                    break;
                }
            }
        }

        //获取到原始sql语句
        String sql = boundSql.getSql();
        String msql = sql;

        //注解逻辑判断  添加注解了才拦截
        //mapperPath，如com.uv.dao.UserMapper
        String mapperPath = daoPath.substring(0, daoPath.lastIndexOf("."));
        Class<?> classType = Class.forName(mapperPath);
        String mName = daoPath.substring(daoPath.lastIndexOf(".") + 1, daoPath.length());
        boolean flag = false;
        if(classType.isAnnotationPresent(MftccDataAuthority.class)){
            MftccDataAuthority classMDA = classType.getAnnotation(MftccDataAuthority.class);
            if(classMDA.enable()){
                flag = true;
                for (Method method : classType.getDeclaredMethods()) {
                    if(mName.equals(method.getName())){
                        if (method.isAnnotationPresent(MftccDataAuthority.class)) {
                            MftccDataAuthority methodMDA = method.getAnnotation(MftccDataAuthority.class);
                            if (!methodMDA.enable()) {
                                flag = false;
                            }else{
                                flag = true;
                            }
                        }else{
                            flag = true;
                        }
                        break;
                    }
                }
            }
        }else{
            for (Method method : classType.getDeclaredMethods()) {
                if(mName.equals(method.getName())){
                    if (method.isAnnotationPresent(MftccDataAuthority.class)) {
                        MftccDataAuthority methodMDA = method.getAnnotation(MftccDataAuthority.class);
                        if (!methodMDA.enable()) {
                            flag = false;
                        }else{
                            flag = true;
                        }
                    }else{
                        flag = false;
                    }
                    break;
                }
            }
        }

        if(flag){
            if(null != page && page.getSize() >= 0L) {
                Connection connection = (Connection)invocation.getArgs()[0];
                DbType dbType = StringUtils.isNotEmpty(this.dialectType) ? DbType.getDbType(this.dialectType) : com.baomidou.mybatisplus.extension.toolkit.JdbcUtils.getDbType(connection.getMetaData().getURL());
                boolean orderBy = true;
                if (page.isSearchCount()) {
                    SqlInfo sqlInfo = SqlParserUtils.getOptimizeCountSql(page.optimizeCountSql(), this.sqlParser, sql);
                    orderBy = sqlInfo.isOrderBy();
                    String pageSql = this.process(mapperPath, mName, sqlInfo.getSql());
                    this.queryTotal(this.overflow, pageSql, mappedStatement, boundSql, page, connection);
//                    if (page.getTotal() <= 0L) {
//                        return invocation.proceed();
//                    }
                }
                sql = this.process(mapperPath, mName, sql);

                if(!sql.endsWith("LIMIT ?, ?")){
                    String buildSql = concatOrderBy(sql, page, orderBy);
                    DialectModel model = DialectFactory.buildPaginationSql(page, buildSql, dbType, this.dialectClazz);
                    Configuration configuration = mappedStatement.getConfiguration();
                    List<ParameterMapping> mappings = new ArrayList(boundSql.getParameterMappings());
                    Map<String, Object> additionalParameters = (Map)metaObject.getValue("delegate.boundSql.additionalParameters");
                    model.consumers(mappings, configuration, additionalParameters);
                    metaObject.setValue("delegate.boundSql.parameterMappings", mappings);
                    msql = model.getDialectSql();
                }else{
                    msql = sql;
                }
            }else{
                msql = this.process(mapperPath, mName, sql);
            }
        }else{
            if (null != page && page.getSize() >= 0L) {
                String originalSql = boundSql.getSql();
                Connection connection = (Connection)invocation.getArgs()[0];
                DbType dbType = StringUtils.isNotEmpty(this.dialectType) ? DbType.getDbType(this.dialectType) : com.baomidou.mybatisplus.extension.toolkit.JdbcUtils.getDbType(connection.getMetaData().getURL());
                boolean orderBy = true;
                if (page.isSearchCount()) {
                    SqlInfo sqlInfo = SqlParserUtils.getOptimizeCountSql(page.optimizeCountSql(), this.sqlParser, originalSql);
                    orderBy = sqlInfo.isOrderBy();
                    this.queryTotal(this.overflow, sqlInfo.getSql(), mappedStatement, boundSql, page, connection);
                    if (page.getTotal() <= 0L) {
                        return invocation.proceed();
                    }
                }

                String buildSql = concatOrderBy(originalSql, page, orderBy);
                DialectModel model = DialectFactory.buildPaginationSql(page, buildSql, dbType, this.dialectClazz);
                Configuration configuration = mappedStatement.getConfiguration();
                List<ParameterMapping> mappings = new ArrayList(boundSql.getParameterMappings());
                Map<String, Object> additionalParameters = (Map)metaObject.getValue("delegate.boundSql.additionalParameters");
                model.consumers(mappings, configuration, additionalParameters);
                metaObject.setValue("delegate.boundSql.sql", model.getDialectSql());
                metaObject.setValue("delegate.boundSql.parameterMappings", mappings);
                return invocation.proceed();
            } else {
                return invocation.proceed();
            }
        }

        metaObject.setValue("delegate.boundSql.sql", msql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return (target instanceof RoutingStatementHandler) ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    //处理数据权限
    private String process(String mapperPath, String methodName, String sql){
        String opNo = requestUtil.getUserInfo("opNo").toString();
        String roleNo = requestUtil.getUserInfo("roleNo").toString();
        String brNo = requestUtil.getUserInfo("brNo").toString();
        String corpId = requestUtil.getUserInfo("corpId").toString();

        JSONArray authArray = new JSONArray();
//        JSONObject jsonObject = null;
        String[] roleNoArr = roleNo.split("\\|");
        for(String rno : roleNoArr){
            String roleId = corpId + rno;
            JSONArray jsonArray = authorityUtil.getAuthArray(roleId,"dataAuth");
            for(int i=0;i<jsonArray.size();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String mapperPaths = object.getString("mapperPath");
                if((mapperPath+"."+methodName).equals(mapperPaths)){
//                    if(jsonObject == null ){
//                        jsonObject = object;
//                    }
                    authArray.add(object);
                }
            }
        }

        Map<String,Object> columnMap = new HashMap<>();
        if(authArray.size()>0){
            for(int a=0;a<authArray.size();a++){
                JSONObject jsonObject = authArray.getJSONObject(a);
                JSONArray dataAuthArr = jsonObject.getJSONArray("dataAuth");
                String authType = jsonObject.getString("authType");
                //全部
                if("99".equals(authType)){
                    return sql;
                }
                String authField = "";
                String type = "";
                JSONArray brNos = new JSONArray();
                if(!"0".equals(authType)){
                    for(int i=0;i<dataAuthArr.size();i++){
                        if(authType.equals(dataAuthArr.getJSONObject(i).getString("id"))){
                            authField = dataAuthArr.getJSONObject(i).getString("field");
                            type = dataAuthArr.getJSONObject(i).getString("type");
                            brNos = dataAuthArr.getJSONObject(i).getJSONArray("brNos");
                            break;
                        }
                    }
                }
                switch (authType){
                    //本人 - reg_op_no
                    case "1":
                        columnMap.put(authField,opNo);
                        break;
                    //本级 - reg_br_no
                    case "4":
                        columnMap.put(authField,brNo);
                        break;
                    //本级及下级 - reg_br_no
                    case "7":
                        List<String> brNoList = (List<String>)requestUtil.getUserInfo("brNoChildren");
                        columnMap.put(authField,brNoList);
                        break;
                    //本法人 - corp_id
                    case "10":
                        columnMap.put(authField,corpId);
                        break;
                    default:
                        Object value = "";
                        switch (type){
                            case "1":
                                value = opNo;
                                break;
                            case "4":
                                value = brNo;
                                break;
                            case "7":
                                List<String> brNoList2 = (List<String>)requestUtil.getUserInfo("brNoChildren");
                                value = brNoList2;
                                break;
                            case "10":
                                value = corpId;
                                break;
                            case "88":
                                List<String> brNosList = brNos.toJavaList(String.class);
                                value = brNosList;
                                break;
                            default:
                                break;
                        }
                        columnMap.put(authField,value);
                        break;
                }
            }
        }else{
            columnMap.put("1","0");
        }
        sql =  contactConditions(sql, columnMap);
        return sql;
    }

    private String contactConditions(String sql, Map<String, Object> columnMap) {
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcUtils.MYSQL);
        List<SQLStatement> stmtList = parser.parseStatementList();
        SQLStatement stmt = stmtList.get(0);
        StringBuffer constraintsBuffer = new StringBuffer();
        Set<String> keys = columnMap.keySet();
        Iterator<String> keyIter = keys.iterator();

        while(keyIter.hasNext()) {
            String key = keyIter.next();
            String[] fieldArr = key.split(",");
            Object value = columnMap.get(key);
            for(int f=0;f<fieldArr.length;f++){
                String field = fieldArr[f];
                if(value instanceof String){
                    constraintsBuffer.append(field).append(" = '" + value + "'");
                }else if(value instanceof List){
                    List<String> list = (List<String>)value;
                    if(list!=null&&list.size()>0){
                        String str = "(";
                        for(int i=0;i<list.size();i++){
                            str += "'"+list.get(i)+"'";
                            if(i<list.size()-1){
                                str += ",";
                            }
                        }
                        str += ")";
                        constraintsBuffer.append(field).append(" in " + str);
                    }
                }
                if(f<fieldArr.length-1){
                    constraintsBuffer.append(" or ");
                }
            }
            if(keyIter.hasNext()){
                constraintsBuffer.append(" or ");
            }
        }
        SQLExprParser constraintsParser = SQLParserUtils.createExprParser(constraintsBuffer.toString(), JdbcUtils.MYSQL);
        SQLExpr constraintsExpr = constraintsParser.expr();

        if (stmt instanceof SQLSelectStatement){
            SQLSelectStatement selectStmt = (SQLSelectStatement) stmt;

            SQLSelect sqlselect = selectStmt.getSelect();
            SQLSelectQueryBlock query = (SQLSelectQueryBlock) sqlselect.getQuery();
            SQLExpr whereExpr = query.getWhere();
            // 修改where表达式
            if (whereExpr == null) {
                query.setWhere(constraintsExpr);
            } else {
                SQLBinaryOpExpr newWhereExpr = new SQLBinaryOpExpr(whereExpr, SQLBinaryOperator.BooleanAnd, constraintsExpr);
                query.setWhere(newWhereExpr);
            }
            sqlselect.setQuery(query);
            return sqlselect.toString();
        }else if(stmt instanceof MySqlDeleteStatement){
            MySqlDeleteStatement delStmt = (MySqlDeleteStatement) stmt;
            SQLExpr whereExpr = delStmt.getWhere();
            // 修改where表达式
            if (whereExpr == null) {
                delStmt.setWhere(constraintsExpr);
            } else {
                SQLBinaryOpExpr newWhereExpr = new SQLBinaryOpExpr(whereExpr, SQLBinaryOperator.BooleanAnd, constraintsExpr);
                delStmt.setWhere(newWhereExpr);
            }
            return delStmt.toString();
        }else if(stmt instanceof MySqlUpdateStatement){
            MySqlUpdateStatement updateStmt = (MySqlUpdateStatement) stmt;
            SQLExpr whereExpr = updateStmt.getWhere();
            // 修改where表达式
            if (whereExpr == null) {
                updateStmt.setWhere(constraintsExpr);
            } else {
                SQLBinaryOpExpr newWhereExpr = new SQLBinaryOpExpr(whereExpr, SQLBinaryOperator.BooleanAnd, constraintsExpr);
                updateStmt.setWhere(newWhereExpr);
            }
            return updateStmt.toString();
        }else{
            return sql;
        }
    }

    protected void queryTotal(boolean overflowCurrent, String sql, MappedStatement mappedStatement, BoundSql boundSql, IPage page, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            Throwable var8 = null;

            try {
                DefaultParameterHandler parameterHandler = new MybatisDefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), boundSql);
                parameterHandler.setParameters(statement);
                long total = 0L;
                ResultSet resultSet = statement.executeQuery();
                Throwable var13 = null;

                try {
                    if (resultSet.next()) {
                        total = resultSet.getLong(1);
                    }
                } catch (Throwable var38) {
                    var13 = var38;
                    throw var38;
                } finally {
                    if (resultSet != null) {
                        if (var13 != null) {
                            try {
                                resultSet.close();
                            } catch (Throwable var37) {
                                var13.addSuppressed(var37);
                            }
                        } else {
                            resultSet.close();
                        }
                    }

                }

                page.setTotal(total);
                long pages = page.getPages();
                if (overflowCurrent && page.getCurrent() > pages) {
                    page.setCurrent(1L);
                }
            } catch (Throwable var40) {
                var8 = var40;
                throw var40;
            } finally {
                if (statement != null) {
                    if (var8 != null) {
                        try {
                            statement.close();
                        } catch (Throwable var36) {
                            var8.addSuppressed(var36);
                        }
                    } else {
                        statement.close();
                    }
                }

            }

        } catch (Exception var42) {
            throw ExceptionUtils.mpe("Error: Method queryTotal execution error of sql : \n %s \n", var42, new Object[]{sql});
        }
    }

    public static String concatOrderBy(String originalSql, IPage page, boolean orderBy) {
        if (!orderBy || !ArrayUtils.isNotEmpty(page.ascs()) && !ArrayUtils.isNotEmpty(page.descs())) {
            return originalSql;
        } else {
            StringBuilder buildSql = new StringBuilder(originalSql);
            String ascStr = concatOrderBuilder(page.ascs(), " ASC");
            String descStr = concatOrderBuilder(page.descs(), " DESC");
            if (StringUtils.isNotEmpty(ascStr) && StringUtils.isNotEmpty(descStr)) {
                ascStr = ascStr + ", ";
            }

            if (StringUtils.isNotEmpty(ascStr) || StringUtils.isNotEmpty(descStr)) {
                buildSql.append(" ORDER BY ").append(ascStr).append(descStr);
            }

            return buildSql.toString();
        }
    }

    private static String concatOrderBuilder(String[] columns, String orderWord) {
        return ArrayUtils.isNotEmpty(columns) ? (String) Arrays.stream(columns).filter(StringUtils::isNotEmpty).map((i) -> {
            return i + orderWord;
        }).collect(Collectors.joining(",")) : "";
    }

    public MapperInterceptor setSqlParser(final ISqlParser sqlParser) {
        this.sqlParser = sqlParser;
        return this;
    }

    public MapperInterceptor setOverflow(final boolean overflow) {
        this.overflow = overflow;
        return this;
    }

    public MapperInterceptor setDialectType(final String dialectType) {
        this.dialectType = dialectType;
        return this;
    }

    public MapperInterceptor setDialectClazz(final String dialectClazz) {
        this.dialectClazz = dialectClazz;
        return this;
    }
}
