package com.offcn.scwregister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //开启EurekaServer注册中心服务器
public class ScwregisterApplication {

    public static void main(String[] args) {

        SpringApplication.run(ScwregisterApplication.class, args);
    }

}
