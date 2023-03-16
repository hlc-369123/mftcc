/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.codingapi.txlcn.tc;

import com.alibaba.cloud.nacos.discovery.configclient.NacosConfigServerAutoConfiguration;
import com.codingapi.txlcn.common.runner.TxLcnApplicationRunner;
import com.codingapi.txlcn.common.util.ApplicationInformation;
import com.codingapi.txlcn.common.util.id.ModIdProvider;
import com.codingapi.txlcn.logger.TxLoggerConfiguration;
import com.codingapi.txlcn.tracing.TracingAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.env.ConfigurableEnvironment;

@ConditionalOnProperty(name = "tx-lcn.enabled", havingValue = "true",
        matchIfMissing = true)
@AutoConfigureAfter(NacosConfigServerAutoConfiguration.class)
@Configuration
@ComponentScan(
        excludeFilters = {@Filter(
                type = FilterType.ASPECTJ,
                pattern = {"com.codingapi.txlcn.tc.core.transaction.txc..*"}
        )}
)
@Import({TxLoggerConfiguration.class, TracingAutoConfiguration.class})
public class TCAutoConfiguration {

    public TCAutoConfiguration() {
    }

    @Bean
    public ApplicationRunner txLcnApplicationRunner(ApplicationContext applicationContext) {
        return new TxLcnApplicationRunner(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public ModIdProvider modIdProvider(ConfigurableEnvironment environment, @Autowired(required = false) ServerProperties serverProperties) {
        return () -> {
            return ApplicationInformation.modId(environment, serverProperties);
        };
    }
}
