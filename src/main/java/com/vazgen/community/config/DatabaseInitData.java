package com.vazgen.community.config;

import com.vazgen.community.data.jpa.entity.RoleEntity;
import com.vazgen.community.data.jpa.repository.EventRepository;
import com.vazgen.community.data.jpa.repository.RoleRepository;
import com.vazgen.community.data.jpa.repository.UserRepository;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitData {


  private final JdbcTemplate jdbcTemplate;

  private final DataSource dataSource;

  public DatabaseInitData(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.dataSource = dataSource;
  }

  @Bean
  InitializingBean initData() {
    return () -> {

      Long roleCount = jdbcTemplate.queryForObject("SELECT count(*) FROM ROLE", Long.class);

      if (roleCount == 0) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource dataSql = resourceLoader.getResource("classpath:init-data.sql");
        new ResourceDatabasePopulator(dataSql).execute(dataSource);
      }
    };
  }
}


