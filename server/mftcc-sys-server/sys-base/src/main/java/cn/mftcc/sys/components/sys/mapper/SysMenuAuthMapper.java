/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.mapper;

import cn.mftcc.sys.components.sys.entity.SysMenuAuthEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单权限表
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-18 11:35:29
 */
@Mapper
public interface SysMenuAuthMapper extends BaseMapper<SysMenuAuthEntity> {

//    void delAll();
    void insertBatch(List<SysMenuAuthEntity> list);
}
