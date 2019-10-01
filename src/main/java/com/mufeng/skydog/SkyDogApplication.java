package com.mufeng.skydog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
@ImportResource("classpath:beanRefContext.xml")
@EnableAutoConfiguration
@Slf4j
public class SkyDogApplication {
	public static void main(String[] args) {
		SpringApplication.run(SkyDogApplication.class, args);
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
        System.out.println("=================== skyDog successfully start ==========================");
	}
}
