package cn.mftcc.example.components.aaaa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.example.components.aaaa.entity.SysSecUserMarkInfoEntity;

import cn.mftcc.common.exception.ServiceException;

/**
 * 用户记录信息
 *
 * @author guohanchen
 * @email 
 * @date 2023-03-15 14:53:47
 */
public interface SysSecUserMarkInfoService {

    IPage<SysSecUserMarkInfoEntity> findByPage(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException;

    void insert(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException;

    void update(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException;

    SysSecUserMarkInfoEntity findById(String opNo) throws ServiceException;

    void deleteById(String opNo) throws ServiceException;
}

