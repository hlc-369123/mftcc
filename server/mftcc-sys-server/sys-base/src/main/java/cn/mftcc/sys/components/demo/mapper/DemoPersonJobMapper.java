/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.mapper;

import cn.mftcc.common.annotation.MftccDataAuthority;
import cn.mftcc.sys.components.demo.entity.DemoPersonJobEntity;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author guohanchen
 * @email 
 * @date 2020-11-16 17:16:55
 */
@Mapper
public interface DemoPersonJobMapper extends BaseMapper<DemoPersonJobEntity> {

    @MftccDataAuthority
    IPage<DemoPersonJobEntity> selectPage(IPage<DemoPersonJobEntity> page, @Param("ew") Wrapper<DemoPersonJobEntity> queryWrapper);

}
