package jeongjihun.chatmigrationbatch.config.writer;

import jakarta.persistence.EntityManagerFactory;
import jeongjihun.chatmigrationbatch.chatdb.entity.Chat;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Transactional
public class ChatMigrationWriteConfig {

    private final EntityManagerFactory chatEntityManagerFactory;

    public ChatMigrationWriteConfig(EntityManagerFactory chatEntityManagerFactory) {
        this.chatEntityManagerFactory = chatEntityManagerFactory;
    }

    @Bean
    public JpaItemWriter<Chat> chatMigrationItemWriter() {
        return new JpaItemWriterBuilder<Chat>()
                .entityManagerFactory(chatEntityManagerFactory)
                .build();
    }
}
