package jeongjihun.chatmigrationbatch.coredb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jeongjihun.chatmigrationbatch.chatdb.entity.ChatLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_like")
public class CoreChatLike {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "user_Id")
    private Long userId;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ChatLike toChatLike() {
        return new ChatLike(id, userId, chatId, createdAt);
    }

    @Override
    public String toString() {
        return "CoreChatLike{" +
                "id=" + id +
                ", userId=" + userId +
                ", chatId=" + chatId +
                ", createdAt=" + createdAt +
                '}';
    }
}
