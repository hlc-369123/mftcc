/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.init;

import ch.qos.logback.classic.LoggerContext;
import cn.mftcc.common.logger.LoggerMaskFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Slf4j
public class LoggerInit implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        log.info("——————————————————————加载log脱敏器——————————————————————————————");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.addTurboFilter(new LoggerMaskFilter());
    }
}
