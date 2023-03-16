/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import cn.mftcc.sys.components.sys.entity.SysParmKeyEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 字典项键值
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-26 10:20:27
 */
public interface SysParmKeyService {

    IPage<SysParmKeyEntity> findByPage(SysParmKeyEntity sysParmKeyEntity);

    void insert(SysParmKeyEntity sysParmKeyEntity);

    void update(SysParmKeyEntity sysParmKeyEntity);

    SysParmKeyEntity findById(String keyName);

    void deleteById(String keyName);

    List<SysParmKeyEntity> findByMoldIds(List<String> moldIds);

    List<SysParmKeyEntity> findAll();
}

