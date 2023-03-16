/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.feign.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName SysFeignClient
 * @Description
 * @Author 郭涵晨
 */
@FeignClient("mftcc-flowable-server")
public interface FlowableFeignClient {
}
