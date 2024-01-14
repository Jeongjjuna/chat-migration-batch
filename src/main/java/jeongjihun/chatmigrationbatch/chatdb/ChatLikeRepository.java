package jeongjihun.chatmigrationbatch.chatdb;

import jeongjihun.chatmigrationbatch.chatdb.entity.ChatLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatLikeRepository extends JpaRepository<ChatLike, Long> {
}
