/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author guohanchen
 * @email 
 * @date 2020-11-16 14:00:20
 */
@Mapper
public interface DemoMapper extends BaseMapper {
    @Select("SELECT * FROM sys_area ${ew.customSqlSegment}")
    List<Map<String,Object>> selectAreaList(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
