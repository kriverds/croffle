package com.soluvis.croffle.v1.scheduler.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(value = {"com.soluvis.croffle.v1.scheduler.mapper"}, sqlSessionFactoryRef="SchedulerSessionFactory")
public class SchedulerDataSourceConfig {

    @Bean(name = "SchedulerDataSource")
    @ConfigurationProperties(prefix = "com.soluvis.croffle.v1.scheduler")
    DataSource dbDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

    @Bean(name = "SchedulerSessionFactory")
    SqlSessionFactory dbSqlSessionFactory(
             @Qualifier("SchedulerDataSource") DataSource dbDataSource,
             ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dbDataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/scheduler/**/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "SchedulerSessionTemplate")
	SqlSessionTemplate dbSqlSessionTemplate(
			@Qualifier("SchedulerSessionFactory") SqlSessionFactory dbSqlSessionFactory
			){
		return new SqlSessionTemplate(dbSqlSessionFactory);
	}

	@Bean(name = "SchedulerTransactionManager")
	PlatformTransactionManager txManager(
			@Qualifier("SchedulerDataSource") DataSource ds
			){
		return new DataSourceTransactionManager(ds);
	}
}
