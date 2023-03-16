/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.utils.MapperUtil;
import cn.mftcc.common.utils.RequestUtil;
import cn.mftcc.sys.common.constant.PltConstant;
import cn.mftcc.sys.components.sys.entity.SysRoleEntity;
import cn.mftcc.sys.components.sys.entity.SysUserEntity;
import cn.mftcc.sys.components.sys.mapper.SysRoleMapper;
import cn.mftcc.sys.components.sys.service.SysRoleService;
import cn.mftcc.sys.components.sys.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("sysRoleService")
@Transactional(rollbackFor=Exception.class)
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RequestUtil requestUtil;

    @Override
    public IPage<SysRoleEntity> findByPage(SysRoleEntity sysRoleEntity) throws ServiceException {
        try {
            String opNo = requestUtil.getUserInfo("opNo").toString();
            if(!opNo.equals(PltConstant.SUPER_ADMIN)){
                String corpId = requestUtil.getUserInfo("corpId").toString();
                sysRoleEntity.setCorpId(corpId);
            }
            //翻页
            IPage<SysRoleEntity> page = new Page<>();
            page.setCurrent(sysRoleEntity.getPageNo());
            page.setSize(sysRoleEntity.getPageSize());
            QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
            //指定字段查询
            queryWrapper
                    .like(StringUtils.isNotBlank(sysRoleEntity.getRoleName()),"role_name",sysRoleEntity.getRoleName())
                    .like(StringUtils.isNotBlank(sysRoleEntity.getRoleNo()),"role_no",sysRoleEntity.getRoleNo())
                    .like(StringUtils.isNotBlank(sysRoleEntity.getRoleType()),"role_type",sysRoleEntity.getRoleType())
                    .eq(StringUtils.isNotBlank(sysRoleEntity.getCorpId()),"corp_id",sysRoleEntity.getCorpId());

            //动态查询-多列
            MapperUtil.dynamicQuery(queryWrapper, sysRoleEntity.getDynamicQuery(),"role_lvl","role_name","role_no","role_type","corp_id");


            if(sysRoleEntity.getSelectKey()!=null&&sysRoleEntity.getSelectValue()!=null){
                String[] valueArr = sysRoleEntity.getSelectValue().split(",");
                String fieldName = MapperUtil.humpToLine(sysRoleEntity.getSelectKey());
                queryWrapper.in(fieldName, valueArr);
            }

            return sysRoleMapper.selectPage(page,queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysRoleEntity.getRoleId()+"",e);
        }

    }

    @Override
    public IPage<SysRoleEntity> findByPage4admin(SysRoleEntity sysRoleEntity) throws ServiceException {
        try {
            //翻页
            IPage<SysRoleEntity> page = new Page<>();
            page.setCurrent(sysRoleEntity.getPageNo());
            page.setSize(sysRoleEntity.getPageSize());
            QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
            String opNo = requestUtil.getUserInfo("opNo").toString();
            if(!opNo.equals(PltConstant.SUPER_ADMIN)){
                List<String> corpId = (List<String>)requestUtil.getUserInfo("corpIdChildren");
                queryWrapper.in(corpId.size()>0,"corp_id",corpId);
            }
            //指定字段查询
            queryWrapper
                    .like(StringUtils.isNotBlank(sysRoleEntity.getRoleName()),"role_name",sysRoleEntity.getRoleName())
                    .like(StringUtils.isNotBlank(sysRoleEntity.getRoleNo()),"role_no",sysRoleEntity.getRoleNo())
                    .like(StringUtils.isNotBlank(sysRoleEntity.getRoleType()),"role_type",sysRoleEntity.getRoleType());

            //动态查询-多列
            MapperUtil.dynamicQuery(queryWrapper, sysRoleEntity.getDynamicQuery(),"role_lvl","role_name","role_no","role_type","corp_id");


            return sysRoleMapper.selectPage(page,queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysRoleEntity.getRoleId()+"",e);
        }

    }

    @Override
    public List<SysRoleEntity> findAll() throws ServiceException {
        try {
            return sysRoleMapper.selectList(new QueryWrapper<>());
        } catch (Exception e) {
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public void insert(SysRoleEntity sysRoleEntity) throws ServiceException {
        try {
            String corpId = String.valueOf(requestUtil.getUserInfo("corpId"));
            String roleId = corpId + sysRoleEntity.getRoleNo();
            sysRoleEntity.setCorpId(corpId);
            sysRoleEntity.setRoleId(roleId);
            sysRoleMapper.insert(sysRoleEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysRoleEntity.getRoleNo(),e);
        }
    }

    @Override
    public void update(SysRoleEntity sysRoleEntity) throws ServiceException {
        try {
            sysRoleMapper.updateById(sysRoleEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysRoleEntity.getRoleNo(),e);
        }
    }

    @Override
    public SysRoleEntity findById(String roleId) throws ServiceException {
        try {
            return sysRoleMapper.selectById(roleId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,roleId,e);
        }
    }

    @Override
    public SysRoleEntity findByRoleNo(String roleNo) throws ServiceException {
        try {
            QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(StringUtils.isNotBlank(roleNo),"role_no",roleNo);
            return sysRoleMapper.selectOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,roleNo,e);
        }
    }

    @Override
    public void deleteById(String roleId) throws ServiceException {
        SysRoleEntity sysRoleEntity = this.findById(roleId);
        if(sysRoleEntity != null){
            List<SysUserEntity> sysUserEntityList = sysUserService.findByRoleNo(sysRoleEntity.getRoleNo());
            if(sysUserEntityList != null && sysUserEntityList.size() > 0){
                throw new ServiceException(null,null,"已绑定用户，需要解除绑定后才能解除");
            }
        }
        try {
            sysRoleMapper.deleteById(roleId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,roleId,e);
        }
    }

    @Override
    public List<SysRoleEntity> findListByCorpId() throws ServiceException {
        try {
            QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
            String opNo = requestUtil.getUserInfo("opNo").toString();
            if(!opNo.equals(PltConstant.SUPER_ADMIN)){
                String corpId = requestUtil.getUserInfo("corpId").toString();
                queryWrapper.eq("corp_id", corpId);
            }
            return sysRoleMapper.selectList(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public List<SysRoleEntity> findListByRoleIds(List<String> roleIds) throws ServiceException {
        try {
            QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
            if(roleIds.size()>0){
                queryWrapper.in("role_id",roleIds).groupBy("role_type");
            }else{
                queryWrapper.eq("1","0");
            }
            return sysRoleMapper.selectList(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public List<SysRoleEntity> findListByRoleNos(List<String> roleNos) throws ServiceException {
        try {
            QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
            if(roleNos.size()>0){
                queryWrapper.in("role_no",roleNos).groupBy("role_no");
            }else{
                queryWrapper.eq("1","0");
            }
            return sysRoleMapper.selectList(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

    @Override
    public List<SysRoleEntity> findListByCorpId(String corpId) throws ServiceException {
        try {
            QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("corp_id", corpId);

            return sysRoleMapper.selectList(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }


    @Override
    public List<SysRoleEntity> selectList(SysRoleEntity sysRoleEntity) throws ServiceException {
        QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(sysRoleEntity);
        return sysRoleMapper.selectList(queryWrapper);
    }
}