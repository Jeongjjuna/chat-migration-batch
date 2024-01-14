package jeongjihun.chatmigrationbatch.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.chat-db.datasource") // application.yml의 datasource에 맵핑하겠다.(사용하겠다)
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory2", // 아래 정의
        transactionManagerRef = "transactionManager2", // 아래 정의
        basePackages = {"jeongjihun.chatmigrationbatch.chatdb"} // repository기준 패키지경로
)
public class ChatDbConfig extends HikariConfig {

    @Bean(name = "dataSource2")
    @Primary
    public DataSource dataSource2() {
        return new LazyConnectionDataSourceProxy(new HikariDataSource(this));
    }

    @Bean(name = "entityManagerFactory2")
    @Primary
    public EntityManagerFactory entityManagerFactory2(@Qualifier("dataSource2") DataSource dataSource) {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(Map.of(
                "hibernate.show_sql", "true"
        ));

        factory.setPackagesToScan("jeongjihun.chatmigrationbatch.chatdb"); // domain
        factory.setPersistenceUnitName("chatdb");
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager2(@Qualifier("entityManagerFactory2") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }
}
