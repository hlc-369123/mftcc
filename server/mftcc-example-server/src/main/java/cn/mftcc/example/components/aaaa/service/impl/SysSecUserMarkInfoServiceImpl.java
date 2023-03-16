package cn.mftcc.example.components.aaaa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import cn.mftcc.common.utils.MapperUtil;
import org.springframework.transaction.annotation.Transactional;

import cn.mftcc.example.components.aaaa.entity.SysSecUserMarkInfoEntity;
import cn.mftcc.example.components.aaaa.mapper.SysSecUserMarkInfoMapper;
import cn.mftcc.example.components.aaaa.service.SysSecUserMarkInfoService;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.constant.SysConstant;

/**
 * 用户记录信息
 *
 * @author guohanchen
 * @email 
 * @date 2023-03-15 14:53:47
 */
@Service("sysSecUserMarkInfoService")
@Transactional(rollbackFor = ServiceException.class)
public class SysSecUserMarkInfoServiceImpl implements SysSecUserMarkInfoService {

    @Autowired
    private SysSecUserMarkInfoMapper sysSecUserMarkInfoMapper;
    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public IPage<SysSecUserMarkInfoEntity> findByPage(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException {
        try{
            //翻页
            IPage<SysSecUserMarkInfoEntity> page = new Page<>();
            page.setCurrent(sysSecUserMarkInfoEntity.getPageNo());
            page.setSize(sysSecUserMarkInfoEntity.getPageSize());
            QueryWrapper<SysSecUserMarkInfoEntity> queryWrapper = new QueryWrapper<>();
            mapperUtil.tableQuery(queryWrapper,sysSecUserMarkInfoEntity);
            return sysSecUserMarkInfoMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysSecUserMarkInfoEntity.getOpNo(),e);
        }
    }

    @Override
    public void insert(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException {
        try{
            sysSecUserMarkInfoMapper.insert(sysSecUserMarkInfoEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysSecUserMarkInfoEntity.getOpNo(),e);
        }
    }

    @Override
    public void update(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException {
        try{
            sysSecUserMarkInfoMapper.updateById(sysSecUserMarkInfoEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysSecUserMarkInfoEntity.getOpNo(),e);
        }
    }

    @Override
    public SysSecUserMarkInfoEntity findById(String opNo) throws ServiceException {
        try{
            return sysSecUserMarkInfoMapper.selectById(opNo);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,opNo,e);
        }
    }

    @Override
    public void deleteById(String opNo) throws ServiceException {
        try{
            sysSecUserMarkInfoMapper.deleteById(opNo);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,opNo,e);
        }
    }

}