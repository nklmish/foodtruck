package com.nklmish.foodtrucks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@SpringBootApplication
@EnableScheduling
@EnableHystrix
@EnableHystrixDashboard
@EnableSpringDataWebSupport
@EnableHypermediaSupport(type = { HAL })
public class FoodTruckApplication {

	@Value("${pool.size}")
	private int poolSize;

	public static void main(String[] args) {
		SpringApplication.run(FoodTruckApplication.class, args);
	}

	@Bean(destroyMethod = "shutdown")
	public Executor taskScheduler() {
		return Executors.newScheduledThreadPool(poolSize);
	}

	@Bean
	public RestOperations restTemplate() {
		return new RestTemplate();
	}
	
}
