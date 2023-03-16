/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.logger.traceLog.SysTraceLogUtil;
import cn.mftcc.common.logger.traceLog.entity.SysTraceLog;
import cn.mftcc.common.utils.DateUtil;
import cn.mftcc.common.utils.MapperUtil;
import cn.mftcc.common.utils.RequestUtil;
import cn.mftcc.sys.common.constant.PltConstant;
import cn.mftcc.sys.components.sys.entity.SysSecUserMarkInfoEntity;
import cn.mftcc.sys.components.sys.entity.SysUserEntity;
import cn.mftcc.sys.components.sys.mapper.SysUserMapper;
import cn.mftcc.sys.components.sys.service.SysDeptService;
import cn.mftcc.sys.components.sys.service.SysSecUserMarkInfoService;
import cn.mftcc.sys.components.sys.service.SysUserService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("sysUserService")
@Transactional(rollbackFor=Exception.class)
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private SysSecUserMarkInfoService sysSecUserMarkInfoService;

    @Autowired
    private SysTraceLogUtil sysTraceLogUtil;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private MapperUtil MapperUtil;

    @Override
    public IPage<SysUserEntity> findByPage(SysUserEntity sysUserEntity) throws ServiceException {
        try {
            String opNo = requestUtil.getUserInfo("opNo").toString();
            if(!opNo.equals(PltConstant.SUPER_ADMIN)){
                String corpId = requestUtil.getUserInfo("corpId").toString();
                sysUserEntity.setCorpId(corpId);
            }
            //翻页
            IPage<SysUserEntity> page = new Page<>();
            page.setCurrent(sysUserEntity.getPageNo());
            page.setSize(sysUserEntity.getPageSize());
            QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
//            //指定字段查询
            queryWrapper
                    .like(StringUtils.isNotBlank(sysUserEntity.getOpName()),"op_name",sysUserEntity.getOpName())
                    .eq(StringUtils.isNotBlank(sysUserEntity.getCorpNo()),"crop_no",sysUserEntity.getCorpNo())
                    .eq(StringUtils.isNotBlank(sysUserEntity.getTel()),"tel",sysUserEntity.getTel())
                    .like(StringUtils.isNotBlank(sysUserEntity.getOpNo()),"op_no",sysUserEntity.getOpNo())
                    .eq(StringUtils.isNotBlank(sysUserEntity.getUserSts()),"user_sts",sysUserEntity.getUserSts())
                    .like(StringUtils.isNotBlank(sysUserEntity.getRoleNo()),"role_no",sysUserEntity.getRoleNo())
                    .eq(StringUtils.isNotBlank(sysUserEntity.getBrNo()),"br_no",sysUserEntity.getBrNo())
                    .eq(StringUtils.isNotBlank(sysUserEntity.getCorpId()),"corp_id",sysUserEntity.getCorpId());
            return sysUserMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysUserEntity.getUserId()+"",e);
        }
    }

    @Override
    public IPage<SysUserEntity> findByPage4admin(SysUserEntity sysUserEntity) throws ServiceException {
        try {
            //翻页
            IPage<SysUserEntity> page = new Page<>();
            page.setCurrent(sysUserEntity.getPageNo());
            page.setSize(sysUserEntity.getPageSize());
            QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
            String opNo = requestUtil.getUserInfo("opNo").toString();
            if(!opNo.equals(PltConstant.SUPER_ADMIN)){
                List<String> corpId = (List<String>)requestUtil.getUserInfo("corpIdChildren");
                queryWrapper.in(corpId!=null&&corpId.size()>0,"corp_id",corpId);
            }
            //指定字段查询
            queryWrapper
                    .like(StringUtils.isNotBlank(sysUserEntity.getOpName()),"op_name",sysUserEntity.getOpName())
                    .like(StringUtils.isNotBlank(sysUserEntity.getOpNo()),"op_no",sysUserEntity.getOpNo())
                    .eq(StringUtils.isNotBlank(sysUserEntity.getUserSts()),"user_sts",sysUserEntity.getUserSts())
                    .like(StringUtils.isNotBlank(sysUserEntity.getRoleName()),"role_name",sysUserEntity.getRoleName())
                    .like(StringUtils.isNotBlank(sysUserEntity.getBrName()),"br_name",sysUserEntity.getBrName());

            MapperUtil.tableQuery(queryWrapper,sysUserEntity);
            return sysUserMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysUserEntity.getUserId()+"",e);
        }
    }
    private QueryWrapper<SysUserEntity> findByPage4DialogWrapper(JSONObject jsonObject){
        SysUserEntity sysUserEntity = JSONObject.parseObject(jsonObject.toJSONString(), SysUserEntity.class);

        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();

        if(sysUserEntity.getSelectKey()!=null&&sysUserEntity.getSelectValue()!=null){
            String[] valueArr = sysUserEntity.getSelectValue().split(",");
            String fieldName = MapperUtil.humpToLine(sysUserEntity.getSelectKey());
            queryWrapper.in(fieldName, valueArr);
            return queryWrapper;
        }

        String brNos = sysUserEntity.getBrNo();
        if(StringUtils.isNotBlank(brNos)){
            String[] brNoArr = brNos.split(",");
            if(jsonObject.getBoolean("children")){
                List<String> list = new ArrayList<>();
                for(String brNo : brNoArr){
                    List<String> brNoList = sysDeptService.getChildrenBrNoList(brNo,sysUserEntity.getCorpId());
                    list.addAll(brNoList);
                }
                queryWrapper.in(list.size()>0,"u.br_no",list);
            }else{
                //指定字段查询
                queryWrapper
                        .in(brNoArr.length>0,"u.br_no",brNoArr);
            }
        }
        String roleNos = sysUserEntity.getRoleNo();
        if(StringUtils.isNotBlank(roleNos)){
            String[] roleNoArr = roleNos.split(",");
            //指定字段查询
            queryWrapper
                    .in(roleNoArr.length>0,"u.role_no",roleNoArr);
        }
        return queryWrapper;
    }

    @Override
    public IPage<SysUserEntity> findByPage4Dialog(JSONObject jsonObject) {
        SysUserEntity sysUserEntity = JSONObject.parseObject(jsonObject.toJSONString(), SysUserEntity.class);
        //翻页
        IPage<SysUserEntity> page = new Page<>();
        page.setCurrent(sysUserEntity.getPageNo());
        page.setSize(sysUserEntity.getPageSize());
        QueryWrapper<SysUserEntity> queryWrapper = findByPage4DialogWrapper(jsonObject);
        String corpId = requestUtil.getUserInfo("corpId").toString();
        queryWrapper.eq(StringUtils.isNotBlank(corpId),"u.corp_id",corpId);
        //动态查询-多列
        MapperUtil.dynamicQuery(queryWrapper, sysUserEntity.getDynamicQuery(),"op_name","op_no","br_name","role_name");
        queryWrapper.groupBy("u.user_id");
        return sysUserMapper.findByPage4Dialog(page,queryWrapper);
    }
    @Override
    public IPage<SysUserEntity> findAllByPage(JSONObject jsonObject) {
        SysUserEntity sysUserEntity = JSONObject.parseObject(jsonObject.toJSONString(), SysUserEntity.class);
        //翻页
        IPage<SysUserEntity> page = new Page<>();
        page.setCurrent(sysUserEntity.getPageNo());
        page.setSize(sysUserEntity.getPageSize());
        QueryWrapper<SysUserEntity> queryWrapper = findByPage4DialogWrapper(jsonObject);
        //动态查询-多列
        MapperUtil.dynamicQuery(queryWrapper, sysUserEntity.getDynamicQuery(),"op_name","op_no","br_name","role_name");
        queryWrapper.groupBy("u.user_id");
        return sysUserMapper.findByPage4Dialog(page,queryWrapper);
    }
    @Override
    public List<SysUserEntity> findAll(JSONObject sysUserEntity) throws ServiceException {
        try {
            QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
            String corpId = requestUtil.getUserInfo("corpId").toString();
            queryWrapper.eq(StringUtils.isNotBlank(corpId),"corp_id",corpId);
            //动态查询-多列
            MapperUtil.dynamicQuery(queryWrapper, sysUserEntity.getString("dynamicQuery"),"op_name","op_no");

            return sysUserMapper.selectList(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"findAll",e);
        }
    }

    @Override
    public List<SysUserEntity> getList(SysUserEntity sysUserEntity) throws ServiceException{
//        try {
//            QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
//            List<SysUserEntity> list = sysUserMapper.selectList(queryWrapper);
//            return list;
//        } catch (Exception e) {
//            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
//        }
        //翻页
        IPage<SysUserEntity> page = new Page<>();
        page.setCurrent(1);
        page.setSize(-1);
        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("u.user_id");
        IPage<SysUserEntity> byPage4Dialog = sysUserMapper.findByPage4Dialog(page, queryWrapper);
        return byPage4Dialog.getRecords();
    }

    @Override
    public List<SysUserEntity> findNoDeptNoRoleUserList() throws ServiceException{
        try {
            QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNull("br_no");
            queryWrapper.isNull("role_no");
            List<SysUserEntity> list = sysUserMapper.selectList(queryWrapper);
            return list;
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public List<SysUserEntity> findUserListByBrRole(String brNo, String roleNo) throws ServiceException{
        try {
            QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
            String[] brNoArr = brNo.split(",");
            queryWrapper.in(brNoArr.length>0,"d.br_no",brNoArr);
            if(StringUtils.isNotBlank(roleNo)){
                String[] roleNoArr = roleNo.split(",");
                queryWrapper.in(roleNoArr.length>0,"r.ROLE_NO",roleNoArr);
            }
            queryWrapper.groupBy("u.user_id");
            List<SysUserEntity> list = sysUserMapper.findUserListByBrRole(queryWrapper);
            return list;
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public void insert(SysUserEntity sysUserEntity) throws ServiceException {
        try {
            String corpId = String.valueOf(requestUtil.getUserInfo("corpId"));
            sysUserEntity.setCorpId(corpId);
            sysUserMapper.insert(sysUserEntity);
            sysTraceLogUtil.addLog(sysUserEntity.getOpNo(),sysUserEntity.getOpNo(),sysUserEntity.getOpNo(),sysUserEntity.getOpNo(),"新增系统用户");
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysUserEntity.getUserId()+"",e);
        }
    }

    @Override
    public void update(SysUserEntity sysUserEntity) throws ServiceException {
        try {
            sysUserMapper.updateById(sysUserEntity);
            sysTraceLogUtil.addLog(sysUserEntity.getOpNo(),sysUserEntity.getOpNo(),sysUserEntity.getOpNo(),sysUserEntity.getOpNo(),"修改系统用户");
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysUserEntity.getUserId()+"",e);
        }
    }

    @Override
    public void updatePassword(SysUserEntity sysUserEntity) throws ServiceException {
        try {
            String opNo = sysUserEntity.getOpNo();
            UpdateWrapper<SysUserEntity> updateWrapper = new UpdateWrapper<>();
            //指定字段查询
            updateWrapper.eq("op_no",opNo);
            sysUserMapper.update(sysUserEntity,updateWrapper);

            SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity = new SysSecUserMarkInfoEntity();
            sysSecUserMarkInfoEntity.setOpNo(opNo);
            sysSecUserMarkInfoEntity.setPasswordUpdateTime(DateUtil.getDate());
            sysSecUserMarkInfoService.update(sysSecUserMarkInfoEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysUserEntity.getUserId()+"",e);
        }
    }

    @Override
    public SysUserEntity findById(int userId) throws ServiceException {
        try {
            return sysUserMapper.selectById(userId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,userId+"",e);
        }
    }

    @Override
    public void deleteById(int userId) throws ServiceException {
        try {
            sysUserMapper.deleteById(userId);
            String opNo = userId+"";
            SysTraceLog sysTraceLog = new SysTraceLog();
            sysTraceLog.setPageNo(1);
            sysTraceLog.setPageSize(-1);
            IPage<SysTraceLog> byPage = sysTraceLogUtil.findByPage(sysTraceLog);
            sysTraceLogUtil.addLog(opNo,opNo,opNo,opNo,"删除系统用户");
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,userId+"",e);
        }
    }

    @Override
    public SysUserEntity findByOpNo(String opNo) throws ServiceException {
        try {
            return sysUserMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("op_no", opNo));
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,opNo,e);
        }
    }

    @Override
    public List<SysUserEntity> findByBrNo(List<String> brNo) throws ServiceException {
        List<SysUserEntity> sysUserEntityList = null;
        try {
            if(brNo != null && brNo.size()>0){
                sysUserEntityList = sysUserMapper.selectList(new QueryWrapper<SysUserEntity>().in("br_no",brNo));
            }
            return sysUserEntityList;
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,"",e);
        }
    }

    @Override
    public List<SysUserEntity> findByRoleNo(String roleNo) throws ServiceException {
        try {
            return sysUserMapper.selectList(new QueryWrapper<SysUserEntity>().like("role_no", roleNo));
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,roleNo,e);
        }
    }


    @Override
    public List<SysUserEntity> findByRoleNo4MQ(String roleNo) throws ServiceException {
        try {
            if(StringUtils.isNotBlank(roleNo)){
                String[] arr = roleNo.split(",");
                return sysUserMapper.selectList(new QueryWrapper<SysUserEntity>().in("role_no", arr));
            }else{
                return new ArrayList<>();
            }
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,roleNo,e);
        }
    }

    @Override
    public List<SysUserEntity> getUserByRoleTypes(String roleTypes) throws ServiceException{
        try {
            String [] roleTypeArray = roleTypes.split("\\|");
            List<String> roleTypeQuery = new ArrayList<>();
            for(String roleType : roleTypeArray){
                if(!StringUtils.isEmpty(roleType)){
                    roleTypeQuery.add(roleType);
                }
            }
            return sysUserMapper.getUserByRoleTypes(roleTypeQuery);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,null,e);
        }
    }
}