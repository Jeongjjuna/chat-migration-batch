package jeongjihun.chatmigrationbatch;

import jeongjihun.chatmigrationbatch.chatdb.ChatLikeRepository;
import jeongjihun.chatmigrationbatch.chatdb.ChatRepository;
import jeongjihun.chatmigrationbatch.chatdb.entity.Chat;
import jeongjihun.chatmigrationbatch.chatdb.entity.ChatLike;
import jeongjihun.chatmigrationbatch.coredb.CoreChatLikeRepository;
import jeongjihun.chatmigrationbatch.coredb.CoreChatRepository;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChat;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChatLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@SpringBootTest
public class DatabaseTest {


    @Autowired
    private CoreChatRepository coreChatRepository;

    @Autowired
    private CoreChatLikeRepository coreChatLikeRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatLikeRepository chatLikeRepository;

    @DisplayName("DB조회 테스트")
    @Test
    void select() {
        List<Chat> chats = chatRepository.findAll();
        List<CoreChat> coreChats = coreChatRepository.findAll();
        List<ChatLike> chatLikes = chatLikeRepository.findAll();
        List<CoreChatLike> coreChatLikes = coreChatLikeRepository.findAll();

        System.out.println(chats.size());
        System.out.println(coreChats.size());
        System.out.println(chatLikes.size());
        System.out.println(coreChatLikes.size());
    }

    @DisplayName("날짜 생성 테스트")
    @Test
    void create_date() {

        String dateString = "2024-01-13 12:19:40.364181";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime max_date = LocalDateTime.parse(dateString, formatter);

        System.out.println(max_date);
    }
}
