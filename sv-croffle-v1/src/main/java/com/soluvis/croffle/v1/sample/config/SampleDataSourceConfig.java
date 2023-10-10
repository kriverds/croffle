package com.soluvis.croffle.v1.sample.config;

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
@MapperScan(value = {"com.soluvis.croffle.v1.sample.mapper"}, sqlSessionFactoryRef="SampleSessionFactory")
public class SampleDataSourceConfig {

	@Bean(name = "SampleDataSource")
	@ConfigurationProperties(prefix = "com.soluvis.croffle.v1.sample")
	public DataSource dbDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name = "SampleSessionFactory")
	public SqlSessionFactory dbSqlSessionFactory(
			@Qualifier("SampleDataSource") DataSource dbDataSource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dbDataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/sample/**/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "SampleSessionTemplate")
	public SqlSessionTemplate dbSqlSessionTemplate(
			@Qualifier("SampleSessionFactory") SqlSessionFactory dbSqlSessionFactory
			){
		return new SqlSessionTemplate(dbSqlSessionFactory);
	}

	@Bean(name = "SampleTransactionManager")
	public PlatformTransactionManager txManager(
			@Qualifier("SampleDataSource") DataSource ds
			){
		return new DataSourceTransactionManager(ds);
	}
}
