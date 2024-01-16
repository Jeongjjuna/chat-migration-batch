package jeongjihun.chatmigrationbatch.chatdb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChat;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChatLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_like")
public class ChatLike {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public boolean isSame(CoreChatLike coreChatLike) {
        if (!this.id.equals(coreChatLike.getId())) {
            return false;
        }
        if (!this.userId.equals(coreChatLike.getUserId())) {
            return false;
        }
        if (!this.chatId.equals(coreChatLike.getChatId())) {
            return false;
        }
        if (!this.createdAt.isEqual(coreChatLike.getCreatedAt())) {
            return false;
        }
        return true;
    }
}
