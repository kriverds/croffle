package com.soluvis.croffle.v1.gcloud.config;

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
@MapperScan(value = {"com.soluvis.croffle.v1.gcloud.mapper"}, sqlSessionFactoryRef="GCloudSessionFactory")
public class GCloudDataSourceConfig {

	@Bean(name = "GCloudDataSource")
	@ConfigurationProperties(prefix = "com.soluvis.croffle.v1.gcloud")
	DataSource dbDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name = "GCloudSessionFactory")
	SqlSessionFactory dbSqlSessionFactory(
			@Qualifier("GCloudDataSource") DataSource dbDataSource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dbDataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/gcloud/**/*.xml"));
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "GCloudSessionTemplate")
	SqlSessionTemplate dbSqlSessionTemplate(
			@Qualifier("GCloudSessionFactory") SqlSessionFactory dbSqlSessionFactory
			){
		return new SqlSessionTemplate(dbSqlSessionFactory);
	}

	@Bean(name = "GCloudTransactionManager")
	PlatformTransactionManager txManager(
			@Qualifier("GCloudDataSource") DataSource ds
			){
		return new DataSourceTransactionManager(ds);
	}
}
