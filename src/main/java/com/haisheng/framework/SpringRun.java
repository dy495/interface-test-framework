package com.haisheng.framework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.haisheng.framework.dao")
public class SpringRun {

	public static void main(String[] args) {

		SpringApplication.run(SpringRun.class, args);
	}
}
