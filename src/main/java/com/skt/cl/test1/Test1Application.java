package com.skt.cl.test1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
public class Test1Application {

    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);

        InfluxdbTestService influxdbTestService = new InfluxdbTestService();
        influxdbTestService.createTest1();
    }
}
