package jeongjihun.chatmigrationbatch;

import jeongjihun.chatmigrationbatch.chatdb.ChatRepository;
import jeongjihun.chatmigrationbatch.chatdb.entity.Chat;
import jeongjihun.chatmigrationbatch.coredb.CoreChatRepository;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class DatabaseTest {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private CoreChatRepository coreChatRepository;

    @DisplayName("DB조회 테스트")
    @Test
    void select() {
        List<Chat> chats = chatRepository.findAll();
        List<CoreChat> coreChats = coreChatRepository.findAll();

        System.out.println(chats.size());
        System.out.println(coreChats.size());
    }
}
