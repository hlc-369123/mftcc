/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysMsgMoldEntity;

import java.util.List;

/**
 * 错误码类别表
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-27 16:03:49
 */
public interface SysMsgMoldService {

    IPage<SysMsgMoldEntity> findByPage(SysMsgMoldEntity sysMsgMoldEntity);

    void insert(SysMsgMoldEntity sysMsgMoldEntity);

    void update(SysMsgMoldEntity sysMsgMoldEntity);

    SysMsgMoldEntity findById(String moldId);

    void deleteById(String moldId);

    void delBatch(String id);

    List<SysMsgMoldEntity> getList();
}

