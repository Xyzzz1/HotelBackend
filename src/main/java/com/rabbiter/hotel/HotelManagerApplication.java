package com.rabbiter.hotel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@MapperScan(basePackages = "com.rabbiter.hotel.mapper")
@EnableScheduling
public class HotelManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelManagerApplication.class, args);
    }
    //test
}
