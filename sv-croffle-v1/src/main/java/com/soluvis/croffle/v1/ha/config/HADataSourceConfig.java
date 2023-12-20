package com.soluvis.croffle.v1.ha.config;

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
@MapperScan(value = {"com.soluvis.croffle.v1.ha.mapper"}, sqlSessionFactoryRef="HASessionFactory")
public class HADataSourceConfig {

	@Bean(name = "HADataSource")
	@ConfigurationProperties(prefix = "com.soluvis.croffle.v1.ha")
	DataSource dbDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name = "HASessionFactory")
	SqlSessionFactory dbSqlSessionFactory(
			@Qualifier("HADataSource") DataSource dbDataSource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dbDataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/ha/**/*.xml"));
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "HASessionTemplate")
	SqlSessionTemplate dbSqlSessionTemplate(
			@Qualifier("HASessionFactory") SqlSessionFactory dbSqlSessionFactory
			){
		return new SqlSessionTemplate(dbSqlSessionFactory);
	}

	@Bean(name = "HATransactionManager")
	PlatformTransactionManager txManager(
			@Qualifier("HADataSource") DataSource ds
			){
		return new DataSourceTransactionManager(ds);
	}
}
