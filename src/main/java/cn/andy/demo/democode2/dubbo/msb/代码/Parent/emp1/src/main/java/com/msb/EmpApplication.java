package com.msb;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by IBM on 2020/9/27.
 */
@SpringBootApplication
@EnableDubbo
public class EmpApplication {


    public static void main(String[] args) {

        SpringApplication.run(EmpApplication.class , args);

    }
}
