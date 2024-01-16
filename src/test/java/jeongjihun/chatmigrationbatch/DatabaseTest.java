package jeongjihun.chatmigrationbatch;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import jeongjihun.chatmigrationbatch.chatdb.ChatLikeRepository;
import jeongjihun.chatmigrationbatch.chatdb.ChatRepository;
import jeongjihun.chatmigrationbatch.chatdb.entity.Chat;
import jeongjihun.chatmigrationbatch.chatdb.entity.ChatLike;
import jeongjihun.chatmigrationbatch.coredb.CoreChatLikeRepository;
import jeongjihun.chatmigrationbatch.coredb.CoreChatRepository;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChat;
import jeongjihun.chatmigrationbatch.coredb.entity.CoreChatLike;
import jeongjihun.chatmigrationbatch.util.DateGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @DisplayName("엔티티 필드 비교 테스트")
    @Test
    void equals() {
        LocalDateTime time = LocalDateTime.now();
        Chat chat = new Chat(1L, 1L, 1L, "content1", time);
        CoreChat same = new CoreChat(1L, 1L, 1L, "content1", time);
        CoreChat diff1 = new CoreChat(1L, 2L, 1L, "content1", time);
        CoreChat diff2 = new CoreChat(1L, 1L, 1L, "content2", time);
        CoreChat diff3 = new CoreChat(1L, 1L, 1L, "content1", LocalDateTime.now());

        assertAll(
                () -> assertThat(chat.isSame(same)).isTrue(),
                () -> assertThat(chat.isSame(diff1)).isFalse(),
                () -> assertThat(chat.isSame(diff2)).isFalse(),
                () -> assertThat(chat.isSame(diff3)).isFalse()
        );
    }

    @DisplayName("core DB, chat DB의 chat 데이터 비교테스트")
    @Test
    void chat_data_equals() {
        // 최대 날짜 지정
        LocalDateTime maxDateTime = DateGenerator.convert( "2024-01-13 12:19:40.364181");

        // 페이지 지정
        int pageNumber = 0; // 페이지 번호 (0부터 시작)
        int pageSize = 20; // 페이지 크기
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt"); // 정렬 정보
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<CoreChat> standard = coreChatRepository.findCoreChatForTest(maxDateTime, pageable);

        for (int pageNo = 0; pageNo < standard.getTotalPages(); pageNo++) {
            pageable = PageRequest.of(pageNo, pageSize, sort);
            Page<Chat> chats = chatRepository.findChatForTest(maxDateTime, pageable);
            Page<CoreChat> coreChats = coreChatRepository.findCoreChatForTest(maxDateTime, pageable);
            List<Chat> chatContents = chats.getContent();
            List<CoreChat> coreChatContents = coreChats.getContent();

            assertThat(chatContents.size()).isEqualTo(coreChatContents.size());
            for (int idx = 0; idx < chatContents.size(); idx++) {
                boolean result = chatContents.get(idx).isSame(coreChatContents.get(idx));
                assertThat(result).isTrue();
            }
        }
    }

    @DisplayName("DB 쿼리 조회하기")
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

    @DisplayName("날짜 생성하기")
    @Test
    void create_date() {

        String dateString = "2024-01-13 12:19:40.364181";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime max_date = LocalDateTime.parse(dateString, formatter);

        System.out.println(max_date);
    }
}
