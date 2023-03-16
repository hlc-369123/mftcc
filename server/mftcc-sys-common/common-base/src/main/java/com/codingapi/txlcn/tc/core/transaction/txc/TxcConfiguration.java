/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.codingapi.txlcn.tc.core.transaction.txc;

import javax.sql.DataSource;

import com.alibaba.cloud.nacos.discovery.configclient.NacosConfigServerAutoConfiguration;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
@ConditionalOnProperty(name = "tx-lcn.enabled", havingValue = "true",
        matchIfMissing = true)
@AutoConfigureAfter(NacosConfigServerAutoConfiguration.class)
@Configuration
@ComponentScan
public class TxcConfiguration {
    public TxcConfiguration() {
    }

    @Bean
    @Primary
    public QueryRunner queryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }
}
