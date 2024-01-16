package jeongjihun.chatmigrationbatch.config.writer;

import jakarta.persistence.EntityManagerFactory;
import jeongjihun.chatmigrationbatch.chatdb.entity.ChatLike;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Transactional
public class ChatLikeMigrationWriteConfig {

    private final EntityManagerFactory chatEntityManagerFactory;

    public ChatLikeMigrationWriteConfig(final EntityManagerFactory chatEntityManagerFactory) {
        this.chatEntityManagerFactory = chatEntityManagerFactory;
    }

    @Bean
    public JpaItemWriter<ChatLike> chatLikeMigrationItemWriter() {
        return new JpaItemWriterBuilder<ChatLike>()
                .entityManagerFactory(chatEntityManagerFactory)
                .build();
    }
}
