/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.mapper;

import cn.mftcc.sys.components.sys.entity.SysViewAuthEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 视角权限配置
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-18 15:00:10
 */
@Mapper
public interface SysViewAuthMapper extends BaseMapper<SysViewAuthEntity> {
//    void delAll();
    void insertBatch(List<SysViewAuthEntity> list);
}
