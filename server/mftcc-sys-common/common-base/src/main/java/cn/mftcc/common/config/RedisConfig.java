/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Slf4j
public class RedisConfig {

    @Value("${spring.redis.host:nohost}")
    private String hostName;

    @Value("${spring.redis.port:}")
    private Integer port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.lettuce.pool.max-idle:0}")
    private Integer maxIdle;

    @Value("${spring.redis.lettuce.pool.min-idle:0}")
    private Integer minIdle;

    @Value("${spring.redis.lettuce.pool.max-active:0}")
    private Integer maxActive;

    @Value("${spring.redis.lettuce.pool.max-wait:0}")
    private Long maxWait;

    @Value("${spring.redis.timeout:0}")
    private Long timeOut;

    @Value("${spring.redis.lettuce.shutdown-timeout:0}")
    private Long shutdownTimeOut;

    @Value("${spring.redis.sentinel.master:nomaster}")
    private String master;

    @Value("${spring.redis.sentinel.nodes:127.0.0.1}")
    private Set<String> nodes;
    
    @Bean
    public Map<Integer,RedisTemplate<String, Object>> redisTemplateMap (LettuceConnectionFactory redisConnectionFactory){
        Map<Integer,RedisTemplate<String, Object>> redisTemplateMap = new HashMap<>();
        if("nohost".equals(hostName)){
            log.info("——————————————————————————加载redis集群连接——————————————————————————");
        }else {
            log.info("——————————————————————————加载redis单体连接——————————————————————————");
        }
        RedisSerializer stringSerializer = new StringRedisSerializer();
        for (int i = 0; i < 16; i++) {
            RedisConfiguration redisConfiguration;
            if("nohost".equals(hostName)){
                //redis配置
                redisConfiguration = new
                        RedisSentinelConfiguration(master, nodes);
                ((RedisSentinelConfiguration) redisConfiguration).setDatabase(i);
                ((RedisSentinelConfiguration) redisConfiguration).setPassword(password);
            }else{
                //redis配置
                redisConfiguration = new
                        RedisStandaloneConfiguration(hostName, port);
                ((RedisStandaloneConfiguration) redisConfiguration).setDatabase(i);
                ((RedisStandaloneConfiguration) redisConfiguration).setPassword(password);
            }
            //连接池配置
            GenericObjectPoolConfig genericObjectPoolConfig =
                    new GenericObjectPoolConfig();
            genericObjectPoolConfig.setMaxIdle(maxIdle);
            genericObjectPoolConfig.setMinIdle(minIdle);
            genericObjectPoolConfig.setMaxTotal(maxActive);
            genericObjectPoolConfig.setMaxWaitMillis(maxWait);

            //redis客户端配置
            LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                    builder = LettucePoolingClientConfiguration.builder().
                    commandTimeout(Duration.ofMillis(timeOut));

            builder.shutdownTimeout(Duration.ofMillis(shutdownTimeOut));
            builder.poolConfig(genericObjectPoolConfig);
            LettuceClientConfiguration lettuceClientConfiguration = builder.build();

            //根据配置和客户端配置创建连接
            LettuceConnectionFactory lettuceConnectionFactory = new
                    LettuceConnectionFactory(redisConfiguration, lettuceClientConfiguration);
            lettuceConnectionFactory.afterPropertiesSet();

            RedisTemplate redisTemplate = new RedisTemplate();
            redisTemplate.setConnectionFactory(lettuceConnectionFactory);
            redisTemplate.setKeySerializer(stringSerializer);
            redisTemplate.setValueSerializer(stringSerializer);
            redisTemplate.setHashKeySerializer(stringSerializer);
            redisTemplate.setHashValueSerializer(stringSerializer);
            redisTemplate.afterPropertiesSet();
            redisTemplateMap.put(i, redisTemplate);
        }
        return redisTemplateMap;
    }
}
