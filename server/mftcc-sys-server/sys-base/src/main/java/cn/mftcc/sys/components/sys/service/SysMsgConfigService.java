/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysMsgConfigEntity;

import java.util.List;

/**
 * 信息码配置表
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-27 14:10:21
 */
public interface SysMsgConfigService {

    IPage<SysMsgConfigEntity> findByPage(SysMsgConfigEntity sysMsgConfigEntity);

    List<SysMsgConfigEntity> findAll();

    void insert(SysMsgConfigEntity sysMsgConfigEntity);

    void update(SysMsgConfigEntity sysMsgConfigEntity);

    SysMsgConfigEntity findById(String msgCode);

    void deleteById(String msgCode);
}

