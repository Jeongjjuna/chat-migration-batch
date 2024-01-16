package jeongjihun.chatmigrationbatch.coredb;

import jeongjihun.chatmigrationbatch.coredb.entity.CoreChat;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChatLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface CoreChatLikeRepository extends JpaRepository<CoreChatLike, Long> {
    @Query("SELECT c FROM CoreChatLike c WHERE c.createdAt < :targetTime")
    Page<CoreChatLike> findCoreChatLikeForTest(@Param("targetTime") LocalDateTime maxDateTime, Pageable pageable);
}
