/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.mapper;

import cn.mftcc.common.annotation.MftccDataAuthority;
import cn.mftcc.sys.components.demo.entity.DemoLeaseAppProjectInitEntity;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目登记表
 * 
 * @author guohanchen
 * @email 
 * @date 2021-03-19 10:42:18
 */
@Mapper
public interface DemoLeaseAppProjectInitMapper extends BaseMapper<DemoLeaseAppProjectInitEntity> {

    @MftccDataAuthority
    IPage<DemoLeaseAppProjectInitEntity> selectPage(IPage<DemoLeaseAppProjectInitEntity> page, @Param("ew") Wrapper<DemoLeaseAppProjectInitEntity> queryWrapper);

}
