package org.custom.example.config;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.core.dialect.JdbcDialect;
import org.springframework.data.jdbc.core.dialect.JdbcPostgresDialect;

/**
 * Defines DataSource explicitly to prevent common failures:
 * missing jdbcUrl, driver not suitable, and autoconfiguration issues,
 * especially under Spring Boot AOT.
 */
@Configuration
public class DataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public HikariDataSource dataSource() {
        return new HikariDataSource();
    }

    @Bean
    public JdbcDialect jdbcDialect() {
        return JdbcPostgresDialect.INSTANCE;
    }
}
