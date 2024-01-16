package jeongjihun.chatmigrationbatch.coredb;

import jeongjihun.chatmigrationbatch.coredb.entity.CoreChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface CoreChatRepository extends JpaRepository<CoreChat, Long> {
    @Query("SELECT c FROM CoreChat c WHERE c.createdAt < :targetTime")
    Page<CoreChat> findCoreChatForTest(@Param("targetTime") LocalDateTime targetTime, Pageable pageable);
}
