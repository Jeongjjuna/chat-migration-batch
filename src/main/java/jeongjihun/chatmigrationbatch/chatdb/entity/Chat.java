package jeongjihun.chatmigrationbatch.chatdb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat")
public class Chat {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "sender_id")
    private Long senderId;
    @Column(name = "room_id")
    private Long roomId;
    @Column(name = "content")
    private String content;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public boolean isSame(CoreChat coreChat) {
        if (!this.id.equals(coreChat.getId())) {
            return false;
        }
        if (!this.senderId.equals(coreChat.getSenderId())) {
            return false;
        }
        if (!this.roomId.equals(coreChat.getRoomId())) {
            return false;
        }
        if (!this.content.equals(coreChat.getContent())) {
            return false;
        }
        if (!this.createdAt.isEqual(coreChat.getCreatedAt())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) && Objects.equals(senderId, chat.senderId) && Objects.equals(roomId, chat.roomId) && Objects.equals(content, chat.content) && Objects.equals(createdAt, chat.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderId, roomId, content, createdAt);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", roomId=" + roomId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
