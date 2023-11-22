package com.wangchen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class PhoneShoppingMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhoneShoppingMallApplication.class, args);
    }

}
