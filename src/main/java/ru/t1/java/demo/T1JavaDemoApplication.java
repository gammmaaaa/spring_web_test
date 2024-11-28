package ru.t1.java.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@Slf4j
@EntityScan(basePackages = {"ru.t1.java.starter.model", "ru.t1.java.demo.model"})
public class T1JavaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(T1JavaDemoApplication.class, args);
    }

}
