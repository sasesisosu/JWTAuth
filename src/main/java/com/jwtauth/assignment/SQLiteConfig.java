package com.jwtauth.assignment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.dialect.Dialect;

@Configuration
public class SQLiteConfig {

    @Bean
    public Dialect sqliteDialect() {
        return new SQLiteDialect();
    }

}