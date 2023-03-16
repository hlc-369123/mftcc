/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.message;

import org.apache.rocketmq.common.message.Message;

/**
 * @author LIlGG
 * @date 2020/9/18
 */
public class TmpMessage extends Message {
    private final static String MQ_TOPIC_TMP = "mq-server-tmp";

    public TmpMessage(byte[] body) {
        super(MQ_TOPIC_TMP, body);
    }

    public TmpMessage(String tags, byte[] body) {
        super(MQ_TOPIC_TMP, tags, body);
    }
}
