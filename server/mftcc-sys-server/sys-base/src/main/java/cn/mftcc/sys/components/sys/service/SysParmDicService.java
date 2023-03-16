/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import cn.mftcc.sys.components.sys.entity.SysParmDicEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 字典项
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-26 10:34:15
 */
public interface SysParmDicService {

    IPage<SysParmDicEntity> findByPage(SysParmDicEntity sysParmDicEntity);

    public List<SysParmDicEntity> findByKeyName(List<String> keyNameList);

    void insert(SysParmDicEntity sysParmDicEntity);

    void update(SysParmDicEntity sysParmDicEntity);

    SysParmDicEntity findById(String keyName);

    void deleteById(String keyName);

    SysParmDicEntity findByKeyNameAndOptCode(SysParmDicEntity sysParmDicEntity);
}

