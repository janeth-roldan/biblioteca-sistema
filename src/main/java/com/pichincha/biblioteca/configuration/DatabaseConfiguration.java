package com.pichincha.biblioteca.configuration;

import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.jdbc.DataSourceBuilder;

@Configuration
public class DatabaseConfiguration {

  private static DatabaseConfiguration instance;
  private DataSource dataSource;

  private DatabaseConfiguration() {
  }

  public static synchronized DatabaseConfiguration getInstance() {
    if (instance == null) {
      instance = new DatabaseConfiguration();
    }
    return instance;
  }

  @Bean
  public DataSource getDataSource() {
    if (dataSource == null) {
      dataSource = DataSourceBuilder.create()
          .driverClassName("org.h2.Driver")
          .url("jdbc:h2:mem:biblioteca")
          .username("sa")
          .password("")
          .build();
    }
    return dataSource;
  }
}