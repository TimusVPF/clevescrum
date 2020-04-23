package me.clevecord.scrum.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.sql.DataSource;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceConfiguration {

    @Bean
    public DataSource dataSource(
        @Value("${spring.datasource.driver}") String driver,
        @Value("${spring.datasource.url}") String url,
        @Value("${spring.datasource.username}") String username,
        @Value("${spring.datasource.password}") String password) {
        return DataSourceBuilder.create()
            .driverClassName(driver)
            .url(url)
            .username(username)
            .password(password)
            .build();
    }
}
