package com.saferide.insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafeRideInsuranceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafeRideInsuranceApplication.class, args);
        System.out.println("==================================================================");
        System.out.println("🚀 SafeRide Insurance Management System is running on port 8080");
        System.out.println("🌐 Open in browser: http://localhost:8080");
        System.out.println("==================================================================");
    }
}
