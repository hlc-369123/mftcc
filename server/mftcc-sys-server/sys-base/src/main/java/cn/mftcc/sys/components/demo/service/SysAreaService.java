/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.sys.components.demo.entity.SysAreaEntity;

import java.util.List;

/**
 * 融资项目表
 *
 * @author zhanglingnan
 * @email 1670680235@qq.com
 * @date 2020-07-03 16:25:37
 */
public interface SysAreaService {

    List<SysAreaEntity> findByUpLev(SysAreaEntity sysAreaEntity) throws ServiceException;

}

