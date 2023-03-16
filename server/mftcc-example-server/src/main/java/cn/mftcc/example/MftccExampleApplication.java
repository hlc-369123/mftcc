/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.example;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;


@MapperScan("cn.mftcc.**.mapper")
@ComponentScan("cn.mftcc.**")
@EnableDiscoveryClient
@EnableFeignClients//feign接口
@EnableTransactionManagement//开启事务管理
@EnableDistributedTransaction//开启分布式事务
@SpringBootApplication
public class MftccExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MftccExampleApplication.class, args);
	}

	/**
	 * spring web 提供的轻量级的http client
	 * <bean id="restTemplate" class="org.springframework.web.client.RestTemplate"/>
	 * @return
	 */
	@Bean
	@LoadBalanced//开启负载均衡 ribbon
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
