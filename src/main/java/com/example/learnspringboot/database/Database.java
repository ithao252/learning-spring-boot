package com.example.learnspringboot.database;

import com.example.learnspringboot.models.ProductModel;
import com.example.learnspringboot.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    //logger
    private static final Logger logger=  LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                ProductModel product_1= new ProductModel("Iphone 14", 2020, 2000D, "");
//                ProductModel product_2= new ProductModel( "Ipad air green", 2021, 1000D, "");
//                logger.info("insert data: "+productRepository.save(product_1));
//                logger.info("insert data: "+productRepository.save(product_2));
            }
        };
    }
}
