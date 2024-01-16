package jeongjihun.chatmigrationbatch.config.reader;

import jakarta.persistence.EntityManagerFactory;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChatLike;
import jeongjihun.chatmigrationbatch.util.DateGenerator;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;

@Configuration
public class ChatLikeMigrationReaderConfig {

    /*
      CHAT_LIKE_MAX_DATE : dual 업데이트를 시작한 첫 데이터의 업로드 시간
      해당 날짜 이전의 데이터를 모두 읽는다.
    */
    private static final String CHAT_LIKE_MAX_DATE = "2024-01-13 13:51:45.122983";
    private static final int CHUNK_SIZE = 10;

    private final EntityManagerFactory coreEntityManagerFactory;

    public ChatLikeMigrationReaderConfig(@Qualifier("entityManagerFactory") EntityManagerFactory coreEntityManagerFactory) {
        this.coreEntityManagerFactory = coreEntityManagerFactory;
    }

    @Bean
    public JpaPagingItemReader<CoreChatLike> chatLikeMigrationItemReader() {
        return new JpaPagingItemReaderBuilder<CoreChatLike>()
                .name("chatLike_JpaPageJob_Reader")
                .entityManagerFactory(coreEntityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("SELECT c FROM CoreChatLike c WHERE c.createdAt < :targetTime ORDER BY c.createdAt ASC")
                .parameterValues(Collections.singletonMap("targetTime", DateGenerator.convert(CHAT_LIKE_MAX_DATE)))
                .build();
    }
}
