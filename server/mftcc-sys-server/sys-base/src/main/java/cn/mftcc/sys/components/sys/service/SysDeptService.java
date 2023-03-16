/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.sys.components.sys.entity.SysDeptEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface SysDeptService {
    
    IPage<SysDeptEntity> findByPage(SysDeptEntity sysDeptEntity) throws ServiceException;

    IPage<SysDeptEntity> findAllByPage(SysDeptEntity sysDeptEntity) throws ServiceException;

    void insert(SysDeptEntity sysDeptEntity) throws ServiceException;

    void update(SysDeptEntity sysDeptEntity) throws ServiceException;

    SysDeptEntity findById(String moldId) throws ServiceException;

    void deleteById(String moldId) throws ServiceException;

    void delBatch(Integer id) throws ServiceException;

    List<SysDeptEntity> getAll() throws ServiceException;

    List<SysDeptEntity> getList(SysDeptEntity sysDeptEntity) throws ServiceException;

    List<String> getChildrenBrNoList(String brNo, String corpId) throws ServiceException;

    List<String> getChildrenCorpIdList(String corpId) throws ServiceException;

    List<SysDeptEntity> getListByCorpId() throws ServiceException;

    SysDeptEntity findByBrNo(String brNo);

    SysDeptEntity findByCorpId(String corpId);

    IPage<SysDeptEntity> findByPage4admin(SysDeptEntity sysDeptEntity);

}
