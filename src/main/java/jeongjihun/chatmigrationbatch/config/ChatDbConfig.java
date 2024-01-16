package jeongjihun.chatmigrationbatch.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "chatEntityManagerFactory",
        transactionManagerRef = "chatTransactionManager",
        basePackages = {"jeongjihun.chatmigrationbatch.chatdb"}) // application.yml의 datasource에 맵핑하겠다.(사용하겠다)
public class ChatDbConfig {

    @Primary
    @Bean(name = "chatDataSource")
    @ConfigurationProperties(prefix = "spring.chat-db.datasource")
    public DataSource dataSource2() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "chatEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean chatEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("chatDataSource") DataSource dataSource) {
        Map<String, String> properties = new HashMap<String, String>();

        return builder.dataSource(dataSource)
                .packages("jeongjihun.chatmigrationbatch.chatdb")
                .persistenceUnit("chatdb")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "chatTransactionManager")
    PlatformTransactionManager chatTransactionManager(@Qualifier("chatEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
