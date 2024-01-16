package jeongjihun.chatmigrationbatch.coredb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jeongjihun.chatmigrationbatch.chatdb.entity.Chat;
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
public class CoreChat {
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

    public Chat toChat() {
        return new Chat(id, senderId, roomId, content, createdAt);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CoreChat coreChat = (CoreChat) o;
        return Objects.equals(id, coreChat.id) && Objects.equals(senderId, coreChat.senderId) && Objects.equals(roomId, coreChat.roomId) && Objects.equals(content, coreChat.content) && Objects.equals(createdAt, coreChat.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderId, roomId, content, createdAt);
    }

    @Override
    public String toString() {
        return "CoreChat{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", roomId=" + roomId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
