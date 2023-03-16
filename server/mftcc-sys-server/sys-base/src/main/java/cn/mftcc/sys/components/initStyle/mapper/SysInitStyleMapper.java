/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.initStyle.mapper;

import cn.mftcc.sys.components.initStyle.entity.SysInitStyleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author guohanchen
 * @email
 * @date 2021-05-27 08:34:16
 */
@Mapper
public interface SysInitStyleMapper extends BaseMapper<SysInitStyleEntity> {

    List<SysInitStyleEntity> findListByCompanyId(@Param("companyId") String companyId);

    void deleteByCompanyId(@Param("companyId") String companyId);

}
