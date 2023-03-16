/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.utils.RequestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import cn.mftcc.sys.components.sys.entity.SysUserSearchFilterEntity;
import cn.mftcc.sys.components.sys.mapper.SysUserSearchFilterMapper;
import cn.mftcc.sys.components.sys.service.SysUserSearchFilterService;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.constant.SysConstant;

import java.util.List;

/**
 * 筛选条件存储表
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-05-23 09:42:01
 */
@Service("sysUserSearchFilterService")
@Transactional(rollbackFor = ServiceException.class)
public class SysUserSearchFilterServiceImpl implements SysUserSearchFilterService {

    @Autowired
    private SysUserSearchFilterMapper sysUserSearchFilterMapper;

    @Autowired
    private RequestUtil requestUtil;

    @Override
    public IPage<SysUserSearchFilterEntity> findByPage(SysUserSearchFilterEntity sysUserSearchFilterEntity) throws ServiceException {
        try{
            //翻页
            IPage<SysUserSearchFilterEntity> page = new Page<>();
            page.setCurrent(sysUserSearchFilterEntity.getPageNo());
            page.setSize(sysUserSearchFilterEntity.getPageSize());
            QueryWrapper<SysUserSearchFilterEntity> queryWrapper = new QueryWrapper<>();
            //指定字段查询
            queryWrapper
                .eq(StringUtils.isNotBlank(sysUserSearchFilterEntity.getFilterNo()),"FILTER_NO",sysUserSearchFilterEntity.getFilterNo())
                .eq(StringUtils.isNotBlank(sysUserSearchFilterEntity.getOpNo()),"OP_NO",sysUserSearchFilterEntity.getOpNo())
                .eq(StringUtils.isNotBlank(sysUserSearchFilterEntity.getVue()),"VUE",sysUserSearchFilterEntity.getVue())
                .eq(StringUtils.isNotBlank(sysUserSearchFilterEntity.getFilterName()),"FILTER_NAME",sysUserSearchFilterEntity.getFilterName());


            return sysUserSearchFilterMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysUserSearchFilterEntity.getFilterNo(),e);
        }
    }

    @Override
    public SysUserSearchFilterEntity insert(SysUserSearchFilterEntity sysUserSearchFilterEntity) throws ServiceException {
        try{
            String opNo = String.valueOf(requestUtil.getUserInfo("opNo"));
            sysUserSearchFilterEntity.setOpNo(opNo);
            sysUserSearchFilterMapper.insert(sysUserSearchFilterEntity);
            return sysUserSearchFilterEntity;
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysUserSearchFilterEntity.getFilterNo(),e);
        }
    }

    @Override
    public void update(SysUserSearchFilterEntity sysUserSearchFilterEntity) throws ServiceException {
        try{
            sysUserSearchFilterMapper.updateById(sysUserSearchFilterEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysUserSearchFilterEntity.getFilterNo(),e);
        }
    }

    @Override
    public SysUserSearchFilterEntity findById(String filterNo) throws ServiceException {
        try{
            return sysUserSearchFilterMapper.selectById(filterNo);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,filterNo,e);
        }
    }

    @Override
    public List<SysUserSearchFilterEntity> findByVue(String vue) throws ServiceException {
        try{
            String opNo = String.valueOf(requestUtil.getUserInfo("opNo"));
            QueryWrapper<SysUserSearchFilterEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("VUE",vue)
                    .eq("OP_NO",opNo);
            return sysUserSearchFilterMapper.selectList(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,vue,e);
        }
    }

    @Override
    public void deleteById(String filterNo) throws ServiceException {
        try{
            sysUserSearchFilterMapper.deleteById(filterNo);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,filterNo,e);
        }
    }

}