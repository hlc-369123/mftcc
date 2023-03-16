/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.codingapi.txlcn.txmsg;

import com.alibaba.cloud.nacos.discovery.configclient.NacosConfigServerAutoConfiguration;
import com.codingapi.txlcn.txmsg.listener.ClientInitCallBack;
import com.codingapi.txlcn.txmsg.listener.DefaultClientInitCallback;
import com.codingapi.txlcn.txmsg.listener.DefaultHeartbeatListener;
import com.codingapi.txlcn.txmsg.listener.DefaultRpcConnectionListener;
import com.codingapi.txlcn.txmsg.listener.HeartbeatListener;
import com.codingapi.txlcn.txmsg.listener.RpcConnectionListener;
import com.codingapi.txlcn.txmsg.loadbalance.RpcLoadBalance;
import com.codingapi.txlcn.txmsg.netty.loadbalance.RandomLoadBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@ConditionalOnProperty(name = "tx-lcn.enabled", havingValue = "true",
        matchIfMissing = true)
@AutoConfigureAfter(NacosConfigServerAutoConfiguration.class)
@Configuration
@ComponentScan
public class MessageConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MessageConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("tx-lcn.message.netty")
    public RpcConfig rpcConfig() {
        return new RpcConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcAnswer rpcClientAnswer() {
        return (rpcCmd) -> {
            log.info("cmd->{}", rpcCmd);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcLoadBalance rpcLoadBalance() {
        return new RandomLoadBalance();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientInitCallBack clientInitCallBack() {
        return new DefaultClientInitCallback();
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcConnectionListener rpcConnectionListener() {
        return new DefaultRpcConnectionListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public HeartbeatListener heartbeatListener() {
        return new DefaultHeartbeatListener();
    }

    public MessageConfiguration() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof MessageConfiguration)) {
            return false;
        } else {
            MessageConfiguration other = (MessageConfiguration)o;
            return other.canEqual(this);
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof MessageConfiguration;
    }

    @Override
    public int hashCode() {
        int result = 1;
        return result;
    }

    @Override
    public String toString() {
        return "MessageConfiguration()";
    }
}
