/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.mq.utils;

import cn.mftcc.message.TmpMessage;
import cn.mftcc.producer.MQProducer;
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
 * @author LIlGG
 * @date 2020/9/18
 */
@Slf4j
@Component
public class TmpMessageUtil{
    @Value("${mftcc.mq.host:}")
    private String host;
    @Value("${mftcc.mq.port:}")
    private String port;

    /**
     * 发送模板消息
     * @param templateId 模板编号
     * @param templateAttr 模板所需变量
     */
    public void sendTmpMessage(@NotNull String templateId, @NotNull Map<String, Object> templateAttr) {
        sendTmpMessage(templateId, templateAttr, null, null);
    }

    /**
     * 发送模板消息
     * @param templateId 模板编号
     * @param templateAttr 模板所需变量
     * @param channel 消息接收的渠道
     */
    public void sendTmpMessage(@NotNull String templateId, @NotNull Map<String, Object> templateAttr, @Nullable String channel) {
        sendTmpMessage(templateId, templateAttr, channel, null);
    }

    /**
     * 发送模板消息
     * @param templateId 模板编号
     * @param templateAttr 模板所需变量
     * @param subs 接收人
     */
    public void sendTmpMessage(@NotNull String templateId, @NotNull Map<String, Object> templateAttr, @Nullable String... subs) {
        sendTmpMessage(templateId, templateAttr, null, subs);
    }

    /**
     * 发送模板消息
     * @param templateId 模板编号
     * @param templateAttr 模板所需变量
     * @param channel 模板接收的渠道
     * @param subs 接收人
     */
    @SneakyThrows
    public void sendTmpMessage(@NotNull String templateId, @NotNull Map<String, Object> templateAttr, @Nullable String channel, @Nullable String... subs) {
        MQProducer mqProducer = MQProducer.getProducer(host +":"+ port);
        Assert.notNull(templateId);
        Assert.notNull(templateAttr);
        Message msg = createTmpMsg(templateId, templateAttr, channel, subs);
        mqProducer.sendOneway(msg);
        mqProducer = null;
    }

    private static Message createTmpMsg(String templateId, Map templateAttr, String channel, String... subs) {
        Map<String, Object> map = new HashMap<>();
        map.put("tmpId", templateId);
        map.put("tmpAttr", JSON.toJSONString(templateAttr));
        if(channel != null && !channel.equals("")) {
            map.put("channel", channel);
        }
        if(subs != null && subs.length > 0) {
            map.put("subs", StringUtils.join(Arrays.asList(subs), ","));
        }

        byte[] bytes = JSON.toJSONBytes(map);
        return new TmpMessage(bytes);
    }
}
