package jeongjihun.chatmigrationbatch.config;

import jakarta.persistence.EntityManagerFactory;
import jeongjihun.chatmigrationbatch.chatdb.entity.Chat;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChat;
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
    private final LocalDateTime max_date;
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
        this.max_date = LocalDateTime.parse(dateString, formatter);
    }

    @Bean
    public Job migrationJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("migrationJob", jobRepository)
                .start(migrationStep(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Step migrationStep(JobRepository jobRepository, @Qualifier("transactionManager2") PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("migrationStep", jobRepository)
                .<CoreChat, Chat> chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(migrationItemReader())
                .processor(migrationItemProcessor())
                .writer(migrationItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<CoreChat> migrationItemReader() {
        return new JpaPagingItemReaderBuilder<CoreChat>()
                .name("JpaPageJob_Reader")
                .entityManagerFactory(coreEntityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("SELECT c FROM CoreChat c WHERE c.createdAt < :targetTime ORDER BY c.createdAt ASC")
                .parameterValues(Collections.singletonMap("targetTime", max_date))
                .build();
    }

    @Bean
    public ItemProcessor<CoreChat, Chat> migrationItemProcessor() {
        return coreChat -> {
            System.out.println(coreChat);
            return coreChat.toChat();
        };
    }

    @Bean
    public JpaItemWriter<Chat> migrationItemWriter() {
        return new JpaItemWriterBuilder<Chat>()
                .entityManagerFactory(chatEntityManagerFactory)
                .build();
    }
}
