package com.project.atm.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

@EnableJpaRepositories("com.project.atm.repository")
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "com.project.atm.serviceImpl", "com.project.atm.exception", "com.project.atm.mapper" })
@Configuration
public class AppConfig {

	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2ddlAuto;

	@Value("${hibernate.show_sql}")
	private String showSql;

	@Value("${hibernate.dialect}")
	private String dialect;

	@Value("${hibernate.connection.password}")
	private String dbPassword;

	@Value("${hibernate.connection.username}")
	private String dbUser;

	@Value("${hibernate.connection.url}")
	private String dbUrl;

	@Value("${hibernate.connection.driver_class}")
	private String dbDriverClass;

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(dbDriverClass);
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(dbUser);
		dataSource.setPassword(dbPassword);
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource());
		factory.setPackagesToScan("com.project.atm.entity");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(Boolean.parseBoolean(showSql));
		factory.setJpaVendorAdapter(vendorAdapter);

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", dialect);
		jpaProperties.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
		jpaProperties.put("hibernate.connection.autocommit", false);
		jpaProperties.put("hibernate.enable_lazy_load_no_trans", true);
		factory.setJpaProperties(jpaProperties);

		return factory;
	}

	@Bean("transactionManager")
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}