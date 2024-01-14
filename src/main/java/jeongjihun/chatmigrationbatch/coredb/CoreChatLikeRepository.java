package jeongjihun.chatmigrationbatch.coredb;

import jeongjihun.chatmigrationbatch.coredb.entity.CoreChatLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoreChatLikeRepository extends JpaRepository<CoreChatLike, Long> {
}
