package com.unir.teamapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.unir.teamapp.persist.repository.common.ComplexJpaRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = ComplexJpaRepositoryImpl.class)
@EntityScan("com.unir.teamapp")
public class TeamAppApplication extends SpringBootServletInitializer {

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(TeamAppApplication.class, args);
    }
    
}
