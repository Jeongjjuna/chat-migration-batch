package jeongjihun.chatmigrationbatch.coredb;

import jeongjihun.chatmigrationbatch.coredb.entity.CoreChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoreChatRepository extends JpaRepository<CoreChat, Long> {
}
