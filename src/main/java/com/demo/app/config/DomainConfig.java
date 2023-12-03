package com.demo.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan("com.demo.app.domain")
@EnableJpaRepositories("com.demo.app.repository")
@EnableTransactionManagement
public class DomainConfig {
}