package br.com.casadocodigo.loja.conf;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
public class JPAConfiguration {
	
	@Bean
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource, Properties additionalProperties) {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		
		entityManagerFactory.setPackagesToScan("br.com.casadocodigo.loja.models");
		entityManagerFactory.setDataSource(dataSource);
		
		entityManagerFactory
		.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		
//		Properties props = aditionalProperties();
//		entityManagerFactory.setJpaProperties(props);
		entityManagerFactory.setJpaProperties(additionalProperties);
		return entityManagerFactory;
	}
	
	@Bean
	@Profile("dev")
	public DataSource getDataSource(){
		
	    ComboPooledDataSource dataSource = new ComboPooledDataSource();

	    try {
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
			dataSource.setJdbcUrl("jdbc:mysql://localhost/casadocodigo");
			dataSource.setUser("root");
			dataSource.setPassword("root");
			
			dataSource.setInitialPoolSize(3);
			dataSource.setMaxPoolSize(10);
			
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	    
	    return dataSource;
		
	}
	
	@Bean
	@Profile("dev")
	public Properties additionalProperties() {
	    Properties props = new Properties();
	    props.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
	    props.setProperty("hibernate.show_sql", "true");
	    props.setProperty("hibernate.hbm2ddl.auto", "update");
		/**
		 * Second level cache 
		 */
//		props.setProperty("hibernate.cache.use_second_level_cache", "true");
//		props.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
//		props.setProperty("hibernate.cache.use_query_cache", "true");
		
		/** Hibernate Statistics **/
		props.setProperty("hibernate.generate_statistics", "true");
		

	    return props;
	}

	@Bean
	public JpaTransactionManager getTransactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}
}
