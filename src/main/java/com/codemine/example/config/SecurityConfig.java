package com.codemine.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//by enabling this we tell Spring to follow our given security and not the
//one which spring is giving
@EnableWebSecurity
public class SecurityConfig {
/*
In Security Config here we will give our Beans so that we can customize the security
By default Spring will work on SecurityFilterChain so its like
one by one the filters will execute. so first we will create a bean for it

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http.build();
    }

when only given this much we are removing all the security from the application
means the login form will also disapper because it is not doing any filter in the method
 */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        /*
Steps we follow
1. disable csrf. we can do this in 2 ways
         */
        //this http.build(); will return the object of SecurityFilterChain
        return http.build();
    }

}
