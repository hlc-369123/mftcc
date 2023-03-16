/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.mq.utils;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送中转消息
 * @author LIlGG
 * @date 2020/10/14
 */
@Slf4j
@Component
public class TransitMessageUtil {
    @Value("mftcc.mq.host")
    private String host;
    @Value("mftcc.mq.port")
    private String port;

    /**
     * 发送中转消息
     * @param body 消息主体
     * @param subs 接受消息的服务标识
     * @param execClass 消息接收执行类
     */
    public void sendTransitMessage(@NotNull Object body, @NotNull String[] subs, @NotNull String execClass) {
        sendTransitMessage(body, subs, execClass, null);
    }

    /**
     * 发送中转消息
     * @param body 消息主体
     * @param subs 接受消息的服务标识
     * @param execClass 消息接收执行类
     * @param category 消息分类
     */
    @SneakyThrows
    public void sendTransitMessage(@NotNull Object body, @NotNull String[] subs, @NotNull String execClass, @Nullable String category) {
        cn.mftcc.producer.MQProducer mqProducer = cn.mftcc.producer.MQProducer.getProducer(host + port);

        Assert.notNull(body);
        Assert.notNull(subs);
        Assert.notNull(execClass);
        Message msg = createTransitMsg(body, subs, execClass, category);

        mqProducer.sendOneway(msg);
        mqProducer = null;
    }

    private static Message createTransitMsg(Object body, String[] subs, String execClass, String category) {
        Map<String, Object> map = new HashMap<>();
        map.put("body", body);
        map.put("subs", subs);
        map.put("execClass", execClass);
        if(category != null && !"".equals(category)) {
            map.put("category", category);
        }

        if(subs != null && subs.length > 0) {
            map.put("subs", StringUtils.join(Arrays.asList(subs), ","));
        }

        byte[] bytes = JSON.toJSONBytes(map);
        return new TransitMessage(bytes);
    }
}
