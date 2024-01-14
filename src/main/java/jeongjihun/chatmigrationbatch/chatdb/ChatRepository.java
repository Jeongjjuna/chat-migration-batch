package jeongjihun.chatmigrationbatch.chatdb;

import jeongjihun.chatmigrationbatch.chatdb.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
