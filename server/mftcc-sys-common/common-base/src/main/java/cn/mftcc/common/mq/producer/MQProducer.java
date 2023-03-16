/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * @author LIlGG
 * @date 2020/9/18
 */
public class MQProducer extends DefaultMQProducer{
    private volatile static MQProducer producer;

    private MQProducer(String nameSrv){
        super("mftccGroup");
        this.setNamesrvAddr(nameSrv);
        try {
            this.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public static MQProducer getProducer(String nameSrv) {
        if (producer == null) {
            synchronized (MQProducer.class) {
                if (producer == null) {
                    producer = new MQProducer(nameSrv);
                }
            }
        }
        return producer;
    }
}
