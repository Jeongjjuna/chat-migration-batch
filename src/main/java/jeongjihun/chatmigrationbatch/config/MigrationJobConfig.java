package jeongjihun.chatmigrationbatch.config;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MigrationJobConfig {

    private static final String JOB_NAME = "migrationJob";
    private final int CHUNK_SIZE = 10;

    private final PlatformTransactionManager chatTransactionManager;
    private final JobRepository jobRepository;
    private final JpaPagingItemReader chatMigrationItemReader;
    private final JpaPagingItemReader chatLikeMigrationItemReader;
    private final JpaItemWriter chatMigrationItemWriter;
    private final JpaItemWriter chatLikeMigrationItemWriter;

    public MigrationJobConfig(
            @Qualifier("transactionManager2") PlatformTransactionManager chatTransactionManager,
            @Qualifier("chatMigrationItemReader") JpaPagingItemReader<CoreChat>  chatMigrationItemReader,
            @Qualifier("chatLikeMigrationItemReader") JpaPagingItemReader<CoreChatLike> chatLikeMigrationItemReader,
            @Qualifier("chatMigrationItemWriter") JpaItemWriter<Chat>  chatMigrationItemWriter,
            @Qualifier("chatLikeMigrationItemWriter") JpaItemWriter<ChatLike> chatLikeMigrationItemWriter,
            final JobRepository jobRepository) {
        this.chatTransactionManager = chatTransactionManager;
        this.jobRepository = jobRepository;
        this.chatMigrationItemReader = chatMigrationItemReader;
        this.chatLikeMigrationItemReader = chatLikeMigrationItemReader;
        this.chatMigrationItemWriter = chatMigrationItemWriter;
        this.chatLikeMigrationItemWriter = chatLikeMigrationItemWriter;
    }

    @Bean
    public Job migrationJob(JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(chatMigrationStep())
                .next(chatLikeMigrationStep())
                .build();
    }

    @Bean
    public Step chatMigrationStep( ) {
        return new StepBuilder("chatMigrationStep", jobRepository)
                .<CoreChat, Chat> chunk(CHUNK_SIZE, chatTransactionManager)
                .reader(chatMigrationItemReader)
                .processor(chatMigrationItemProcessor())
                .writer(chatMigrationItemWriter)
                .build();
    }

    @Bean
    public Step chatLikeMigrationStep() {
        return new StepBuilder("chatLikeMigrationStep", jobRepository)
                .<CoreChatLike, ChatLike> chunk(CHUNK_SIZE, chatTransactionManager)
                .reader(chatLikeMigrationItemReader)
                .processor(chatLikeMigrationItemProcessor())
                .writer(chatLikeMigrationItemWriter)
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
    public ItemProcessor<CoreChatLike, ChatLike> chatLikeMigrationItemProcessor() {
        return coreChatLike -> {
            System.out.println(coreChatLike);
            return coreChatLike.toChatLike();
        };
    }

}
