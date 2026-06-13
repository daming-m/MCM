package com.example.uavmanager;

import org.apache.shiro.spring.boot.autoconfigure.ShiroAnnotationProcessorAutoConfiguration;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
import org.apache.shiro.spring.boot.autoconfigure.ShiroBeanAutoConfiguration;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    ShiroAutoConfiguration.class,
    ShiroBeanAutoConfiguration.class,
    ShiroWebAutoConfiguration.class,
    ShiroAnnotationProcessorAutoConfiguration.class,
    ShiroWebFilterConfiguration.class
})
public class UavManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UavManagerApplication.class, args);
    }
}

