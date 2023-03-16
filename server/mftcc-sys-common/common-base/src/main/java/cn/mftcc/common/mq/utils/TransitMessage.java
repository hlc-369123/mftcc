/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.mq.utils;

import org.apache.rocketmq.common.message.Message;

/**
 * @author LIlGG
 * @date 2020/9/18
 */
public class TransitMessage extends Message {
    private final static String MQ_TOPIC_TRANSIT = "mq-server-transit";

    public TransitMessage(byte[] body) {
        super(MQ_TOPIC_TRANSIT, body);
    }

    public TransitMessage(String tags, byte[] body) {
        super(MQ_TOPIC_TRANSIT, tags, body);
    }
}
