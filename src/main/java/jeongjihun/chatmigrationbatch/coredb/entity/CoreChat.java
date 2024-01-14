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
