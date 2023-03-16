/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import cn.mftcc.sys.components.sys.entity.SysParmMoldEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 字典项类别
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-25 18:18:49
 */
public interface SysParmMoldService {

    IPage<SysParmMoldEntity> findByPage(SysParmMoldEntity sysParmMoldEntity);

    void insert(SysParmMoldEntity sysParmMoldEntity);

    void update(SysParmMoldEntity sysParmMoldEntity);

    SysParmMoldEntity findById(String moldId);

    void deleteById(String moldId);

    void delBatch(String id);

    List<SysParmMoldEntity> getList();
}

