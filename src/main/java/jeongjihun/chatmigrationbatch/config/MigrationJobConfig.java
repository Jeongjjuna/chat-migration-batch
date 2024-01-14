package jeongjihun.chatmigrationbatch.config;

import jakarta.persistence.EntityManagerFactory;
import jeongjihun.chatmigrationbatch.chatdb.entity.Chat;
import jeongjihun.chatmigrationbatch.chatdb.entity.ChatLike;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChat;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChatLike;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Configuration
public class MigrationJobConfig {

    private final int CHUNK_SIZE = 10;
    private final LocalDateTime ChatMaxDate;
    private final LocalDateTime chatLikeMaxDate;


    private final EntityManagerFactory coreEntityManagerFactory; // CLOUD DB 용
    private final EntityManagerFactory chatEntityManagerFactory; // 로컬 DB 용

    public MigrationJobConfig(
            @Qualifier("entityManagerFactory") final EntityManagerFactory coreEntityManagerFactory,
            @Qualifier("entityManagerFactory2") final EntityManagerFactory chatEntityManagerFactory) {
        this.coreEntityManagerFactory = coreEntityManagerFactory;
        this.chatEntityManagerFactory = chatEntityManagerFactory;

        // 마이그레이션할 경계 시간값
        String dateString = "2024-01-13 12:19:40.364181";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        this.ChatMaxDate = LocalDateTime.parse(dateString, formatter);

        dateString = "2024-01-13 13:51:45.122983";
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        this.chatLikeMaxDate = LocalDateTime.parse(dateString, formatter);
    }

    @Bean
    public Job migrationJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("migrationJob", jobRepository)
                .start(chatMigrationStep(jobRepository, platformTransactionManager))
                .next(chatLikeMigrationStep(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Step chatLikeMigrationStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("chatLikeMigrationStep", jobRepository)
                .<CoreChatLike, ChatLike> chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(chatLikeMigrationItemReader())
                .processor(chatLikeMigrationItemProcessor())
                .writer(chatLikeMigrationItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<CoreChatLike> chatLikeMigrationItemReader() {
        return new JpaPagingItemReaderBuilder<CoreChatLike>()
                .name("chatLike_JpaPageJob_Reader")
                .entityManagerFactory(coreEntityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("SELECT c FROM CoreChatLike c WHERE c.createdAt < :targetTime ORDER BY c.createdAt ASC")
                .parameterValues(Collections.singletonMap("targetTime", chatLikeMaxDate))
                .build();
    }

    @Bean
    public ItemProcessor<CoreChatLike, ChatLike> chatLikeMigrationItemProcessor() {
        return coreChatLike -> {
            System.out.println(coreChatLike);
            return coreChatLike.toChatLike();
        };
    }

    @Bean
    public JpaItemWriter<ChatLike> chatLikeMigrationItemWriter() {
        return new JpaItemWriterBuilder<ChatLike>()
                .entityManagerFactory(chatEntityManagerFactory)
                .build();
    }

    @Bean
    public Step chatMigrationStep(JobRepository jobRepository, @Qualifier("transactionManager2") PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("chatMigrationStep", jobRepository)
                .<CoreChat, Chat> chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(chatMigrationItemReader())
                .processor(chatMigrationItemProcessor())
                .writer(chatMigrationItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<CoreChat> chatMigrationItemReader() {
        return new JpaPagingItemReaderBuilder<CoreChat>()
                .name("Chat_JpaPageJob_Reader")
                .entityManagerFactory(coreEntityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("SELECT c FROM CoreChat c WHERE c.createdAt < :targetTime ORDER BY c.createdAt ASC")
                .parameterValues(Collections.singletonMap("targetTime", ChatMaxDate))
                .build();
    }

    @Bean
    public ItemProcessor<CoreChat, Chat> chatMigrationItemProcessor() {
        return coreChat -> {
            System.out.println(coreChat);
            return coreChat.toChat();
        };
    }

    @Bean
    public JpaItemWriter<Chat> chatMigrationItemWriter() {
        return new JpaItemWriterBuilder<Chat>()
                .entityManagerFactory(chatEntityManagerFactory)
                .build();
    }
}
