package com.douzone.final_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableBatchProcessing //Spring Batch 기능 활성화
@EnableScheduling // scheduling 기능 활성화
@SpringBootApplication
public class FinalBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalBackendApplication.class, args);
    }

}
