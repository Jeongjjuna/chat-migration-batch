package jeongjihun.chatmigrationbatch.chatdb;

import jeongjihun.chatmigrationbatch.chatdb.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE c.createdAt < :targetTime")
    Page<Chat> findChatForTest(@Param("targetTime") LocalDateTime targetTime, Pageable pageable);
}
