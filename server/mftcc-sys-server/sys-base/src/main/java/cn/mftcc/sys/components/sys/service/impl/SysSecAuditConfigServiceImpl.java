/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.sysutils.MD5Util;
import cn.mftcc.common.utils.DateUtil;
import cn.mftcc.common.utils.MapperUtil;
import cn.mftcc.common.utils.RequestUtil;
import cn.mftcc.sys.common.constant.PltConstant;
import cn.mftcc.sys.components.sys.entity.SysSecAuditConfigEntity;
import cn.mftcc.sys.components.sys.entity.SysSecUserMarkInfoEntity;
import cn.mftcc.sys.components.sys.entity.SysUserEntity;
import cn.mftcc.sys.components.sys.mapper.SysSecAuditConfigMapper;
import cn.mftcc.sys.components.sys.service.SysSecAuditConfigService;
import cn.mftcc.sys.components.sys.service.SysSecUserMarkInfoService;
import cn.mftcc.sys.components.sys.service.SysUserService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登陆校验规则表
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-06-03 13:42:15
 */
@Service("sysSecAuditConfigService")
@Transactional(rollbackFor = ServiceException.class)
public class SysSecAuditConfigServiceImpl implements SysSecAuditConfigService {

    @Autowired
    private SysSecAuditConfigMapper sysSecAuditConfigMapper;

    @Autowired
    private SysSecUserMarkInfoService sysSecUserMarkInfoService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RequestUtil requestUtil;

    @Override
    public IPage<SysSecAuditConfigEntity> findByPage(SysSecAuditConfigEntity sysSecAuditConfigEntity) throws ServiceException {
        try{
            //翻页
            IPage<SysSecAuditConfigEntity> page = new Page<>();
            page.setCurrent(sysSecAuditConfigEntity.getPageNo());
            page.setSize(sysSecAuditConfigEntity.getPageSize());
            QueryWrapper<SysSecAuditConfigEntity> queryWrapper = new QueryWrapper<>();
            //指定字段查询
            queryWrapper
                .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getItemNo()),"item_no",sysSecAuditConfigEntity.getItemNo())
                .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getCodeType()),"code_type",sysSecAuditConfigEntity.getCodeType())
                .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getItemName()),"item_name",sysSecAuditConfigEntity.getItemName())
                .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getItemValues()),"item_values",sysSecAuditConfigEntity.getItemValues())
                .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getIsUse()),"is_use",sysSecAuditConfigEntity.getIsUse())
                .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getIsEdit()),"is_edit",sysSecAuditConfigEntity.getIsEdit());

            //动态查询-多列
            MapperUtil.dynamicQuery(queryWrapper, sysSecAuditConfigEntity.getDynamicQuery(),"item_no","code_type","item_name","item_values","is_use","is_edit");
            return sysSecAuditConfigMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysSecAuditConfigEntity.getItemNo(),e);
        }
    }

    @Override
    public List<SysSecAuditConfigEntity> findList(SysSecAuditConfigEntity sysSecAuditConfigEntity) throws ServiceException {
        try{
            QueryWrapper<SysSecAuditConfigEntity> queryWrapper = new QueryWrapper<>();
            //指定字段查询
            queryWrapper
                    .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getItemNo()),"item_no",sysSecAuditConfigEntity.getItemNo())
                    .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getCodeType()),"code_type",sysSecAuditConfigEntity.getCodeType())
                    .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getItemName()),"item_name",sysSecAuditConfigEntity.getItemName())
                    .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getItemValues()),"item_values",sysSecAuditConfigEntity.getItemValues())
                    .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getIsUse()),"is_use",sysSecAuditConfigEntity.getIsUse())
                    .eq(StringUtils.isNotBlank(sysSecAuditConfigEntity.getIsEdit()),"is_edit",sysSecAuditConfigEntity.getIsEdit());

            //动态查询-多列
            MapperUtil.dynamicQuery(queryWrapper, sysSecAuditConfigEntity.getDynamicQuery(),"item_no","code_type","item_name","item_values","is_use","is_edit");
            return sysSecAuditConfigMapper.selectList(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysSecAuditConfigEntity.getItemNo(),e);
        }
    }

    @Override
    public void insert(SysSecAuditConfigEntity sysSecAuditConfigEntity) throws ServiceException {
        try{
            sysSecAuditConfigMapper.insert(sysSecAuditConfigEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysSecAuditConfigEntity.getItemNo(),e);
        }
    }

    @Override
    public void update(SysSecAuditConfigEntity sysSecAuditConfigEntity) throws ServiceException {
        try{
            sysSecAuditConfigMapper.updateById(sysSecAuditConfigEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysSecAuditConfigEntity.getItemNo(),e);
        }
    }

    @Override
    public SysSecAuditConfigEntity findById(String itemNo) throws ServiceException {
        try{
            return sysSecAuditConfigMapper.selectById(itemNo);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,itemNo,e);
        }
    }

    @Override
    public void deleteById(String itemNo) throws ServiceException {
        try{
            sysSecAuditConfigMapper.deleteById(itemNo);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,itemNo,e);
        }
    }

    @Override
    public JSONObject validatePWAjax(String opNo,String password) throws ServiceException {
        try{
            JSONObject result = new JSONObject();
            String msg = "";
            SysSecAuditConfigEntity sysSecAuditConfigEntity = new SysSecAuditConfigEntity();
            sysSecAuditConfigEntity.setCodeType("PR");
            sysSecAuditConfigEntity.setIsUse("1");
            List<SysSecAuditConfigEntity> SysSecAuditConfigList = this.findList(sysSecAuditConfigEntity);
            for(SysSecAuditConfigEntity SysSecAuditConfigEntity : SysSecAuditConfigList){
                switch (SysSecAuditConfigEntity.getItemNo()){
                    case PltConstant.PR_LENGTH:
                        int len = new Integer(SysSecAuditConfigEntity.getItemValues());
                        if(password.length()<len){
                            msg += "密码最小长度为"+SysSecAuditConfigEntity.getItemValues()+",";
                        }
                        break;
                    case PltConstant.PR_UPPER_LETTERS:
                        if(!match("[A-Z]+?",password)){
                            msg += "必须包含英文大写字母(A 到 Z),";
                        }
                        break;
                    case PltConstant.PR_LOWER_LETTERS:
                        if(!match("[a-z]+?",password)){
                            msg += "必须包含英文小写字母(a 到 z),";
                        }
                        break;
                    case PltConstant.PR_NUMBER:
                        if(!match("[0-9]+?",password)){
                            msg += "必须包含10个基本数字(0 到 9),";
                        }
                        break;
                    case PltConstant.PR_SPECIAL_LETTERS:
                        if(isConSpeCharacters(password)){
                            msg += "必须包含特殊字符(!、@、$、*、.),";
                        }
                        break;
                    case PltConstant.PR_NO_OPNO:
                        if(password.indexOf(opNo)>-1){
                            msg += "不能包含用户的帐户名,";
                        }
                        break;
                    case PltConstant.PR_NO_SAME:
                        SysUserEntity sysUserEntity =sysUserService.findByOpNo(opNo);
                        String newPassword = MD5Util.md5(password);
                        if(newPassword.equals(sysUserEntity.getPassword())){
                            msg += "新密码不能与原密码相同,";
                        }
                        break;
                    default:
                        break;
                }
            }
            if("".equals(msg)){
                result.put("flag", "success");
                result.put("msg", msg);
            }else{
                if(msg.endsWith(",")){
                    msg = msg.substring(0,msg.length()-1);
                }
                result.put("flag", "error");
                result.put("msg", msg);
            }
            return result;
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public JSONObject SecurityPwd(String opNo,String type) throws ServiceException{
        JSONObject JSONObject = new JSONObject();
        try{
            SysSecAuditConfigEntity sysSecAuditConfigEntity = new SysSecAuditConfigEntity();
            sysSecAuditConfigEntity.setIsUse("1");
            List<SysSecAuditConfigEntity> SysSecAuditConfigList = this.findList(sysSecAuditConfigEntity);
            SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity = sysSecUserMarkInfoService.findById(opNo);

            if(sysSecUserMarkInfoEntity!=null){
                Map<String,SysSecAuditConfigEntity> map = new HashMap<>();
                for(int i=0;i<SysSecAuditConfigList.size();i++){
                    SysSecAuditConfigEntity sysSecAuditConfig = SysSecAuditConfigList.get(i);
                    String itemNo = sysSecAuditConfig.getItemNo();
    				map.put(itemNo,sysSecAuditConfig);
                }

                //密码输入错误后的容忍次数
                if(map.containsKey(PltConstant.SF_ERROR_TIMES)){
                    int itemValues = Integer.parseInt(map.get(PltConstant.SF_ERROR_TIMES).getItemValues());
                    if("1".equals(sysSecUserMarkInfoEntity.getPasswordState())){
                        JSONObject.put("flag","error");
                        JSONObject.put("msg","密码连续错误"+itemValues+"次，账号已锁定，请联系系统管理员解锁账号。");
                        return JSONObject;
                    }
                }

                if("pwdRight".equals(type)){
                    //密码最长使用期限；
                    if(map.containsKey(PltConstant.SL_MAX_DATE)){
                        int itemValues = Integer.parseInt(map.get(PltConstant.SL_MAX_DATE).getItemValues());
                        String update = sysSecUserMarkInfoEntity.getPasswordUpdateTime();
                        if(update==null||"".equals(update)){
                            update="1970-01-01";//为防止更新日期为空加起始日
                        }else{
                            update = update.substring(0,4)+"-"+update.substring(4,6)+"-"+update.substring(6,8);
                        }
                        String sysDate = DateUtil.parseEightStrToTen(DateUtil.getDate());
                        if(update==null||"".equals(update)){
                            update="1970-01-01";//为防止更新日期为空加起始日
                        }
                        if(itemValues< DateUtil.getBetweenDays(update,sysDate)){
                            JSONObject.put("flag","toup");
                            JSONObject.put("msg","当前密码已经超过使用期限("+itemValues+"天)，请更换密码：");
                            return JSONObject;
                        }
                    }
                }else if("pwdError".equals(type)){
                    //密码输入错误后的提示次数
                    if(map.containsKey(PltConstant.SF_ERROR_TIMES) && map.containsKey(PltConstant.SF_TIPS_TIMES)){
                        int tipsTimes = Integer.parseInt(map.get(PltConstant.SF_TIPS_TIMES).getItemValues());
                        int errorTimes = Integer.parseInt(map.get(PltConstant.SF_ERROR_TIMES).getItemValues());
                        int loginErrorTimes = sysSecUserMarkInfoEntity.getLoginErrorTimes();
                        if((loginErrorTimes)>=tipsTimes){
                            JSONObject.put("flag","error");
                            JSONObject.put("msg","当前密码已输入错误"+loginErrorTimes+"次，还可输入" + (errorTimes-loginErrorTimes) + "次");
                            return JSONObject;
                        }
                    }
                }else if("mustUpdate".equals(type)){
                    //首次登陆必须修改密码
                    if(map.containsKey(PltConstant.SL_MUST_UPDATE)){
                        if(null == sysSecUserMarkInfoEntity.getLastSignInTime()&&null == sysSecUserMarkInfoEntity.getPasswordUpdateTime()){
                            JSONObject.put("flag","update");
                            JSONObject.put("msg","首次登陆请修改密码");
                            return JSONObject;
                        }
                    }
                }

            }else{//首次登录
                if("mustUpdate".equals(type)){
                    //首次登陆必须修改密码
                    Map<String,SysSecAuditConfigEntity> map = new HashMap<>();
                    for(int i=0;i<SysSecAuditConfigList.size();i++){
                        SysSecAuditConfigEntity sysSecAuditConfig = SysSecAuditConfigList.get(i);
                        String itemNo = sysSecAuditConfig.getItemNo();
                        map.put(itemNo,sysSecAuditConfig);
                    }
                    if(map.containsKey(PltConstant.SL_MUST_UPDATE)){
                        JSONObject.put("flag","update");
                        JSONObject.put("msg","首次登陆请修改密码");
                        return JSONObject;
                    }
                }
                sysSecUserMarkInfoEntity = new SysSecUserMarkInfoEntity();
                sysSecUserMarkInfoEntity.setOpNo(opNo);//操作员号
                sysSecUserMarkInfoEntity.setPasswordState("0");//密码状态
                sysSecUserMarkInfoEntity.setVisitTimes(0);//登录次数
                sysSecUserMarkInfoEntity.setCurrentSignInTime(DateUtil.getDate());//本次登录日期
                sysSecUserMarkInfoEntity.setPasswordUpdateTime(DateUtil.getDate());//初始化
                sysSecUserMarkInfoEntity.setLoginErrorTimes(0);
                sysSecUserMarkInfoEntity.setPasswordMessege("");
                sysSecUserMarkInfoService.insert(sysSecUserMarkInfoEntity);
            }
            JSONObject.put("flag","success");
            JSONObject.put("msg","login");
            return JSONObject;
        }catch(Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_ERROR,"",e);
        }
    }

    @Override
    public String createIdentifyCode(int length,boolean typeMix,String pureNumber,boolean capsLookMix,int numLength,int uupperLength) throws ServiceException {
        String makeCode = "";
        // 数字和字母混合
        if (typeMix){
            // 字母大小写混合
            if (capsLookMix){
                int tempNumLength = numLength < 0 ? 2 : numLength > length ? 2 : numLength;
                int tempUupperLength = uupperLength < 0 ? 1 : uupperLength > length - tempNumLength ? 1 : uupperLength;
                for (int i = 0; i < tempNumLength; i++) {
                    makeCode += this.randomNumber();
                }
                for (int i = 0; i < tempUupperLength; i++) {
                    makeCode += this.randomAlphabet(true);
                }
                if (tempNumLength + tempUupperLength < length) {
                    for (int i = 0; i < length - tempNumLength - tempUupperLength; i++) {
                        makeCode += this.randomAlphabet(false);
                    }
                }
            }else{
                int tempNumLength = numLength < 0 ? 2 : numLength > length ? 2 : numLength;
                for (int i = 0; i < tempNumLength; i++) {
                    makeCode += this.randomNumber();
                }
                if (tempNumLength < length) {
                    for (int i = 0; i < length - tempNumLength; i++) {
                        makeCode += this.randomAlphabet(false);
                    }
                }
            }
        }else{
            if ("number".equals(pureNumber) ) {// 纯数字('number')
                for (int i = 0; i < length; i++) {
                    makeCode += this.randomNumber();
                }
            }else if ("alphabet".equals(pureNumber)) {// 纯字母('alphabet')
                // 字母大小写混合
                if (capsLookMix) {
                    int tempUupperLength = uupperLength < 0 ? 1 : uupperLength > length ? 1 : uupperLength;
                    for (int i = 0; i < tempUupperLength; i++) {
                        makeCode += this.randomAlphabet(true);
                    }
                    if (tempUupperLength < length) {
                        for (int i = 0; i < length - tempUupperLength; i++) {
                            makeCode += this.randomAlphabet(false);
                        }
                    }
                }else {
                    for (int i = 0; i < length; i++) {
                        makeCode += this.randomAlphabet(false);
                    }
                }
            }
        }
        return makeCode;
    }

    private int randomNumber(){
        return (int)(Math.random()*10);
    }

    private String randomAlphabet(boolean isUupper){
        String chars = "abcdefghijklmnopqrstuvwxyz";
        char c = chars.charAt((int)(Math.random() * 26));
        String alphabet = String.valueOf(c);
        if(isUupper){
            return alphabet.toUpperCase();
        }else{
            return alphabet;
        }
    }

    /**
     * @param regex
     *            正则表达式字符串
     * @param str
     *            要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 功能：判断一个字符串是否包含特殊字符
     * @param string 要判断的字符串
     * @return true 提供的参数string不包含特殊字符
     * @return false 提供的参数string包含特殊字符
     */
    private static boolean isConSpeCharacters(String string) {
        if(string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length()==0){
            //如果不包含特殊字符
            return true;
        }
        return false;
    }

}