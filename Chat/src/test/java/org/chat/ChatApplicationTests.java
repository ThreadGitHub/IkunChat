package org.chat;

import org.chat.domain.entity.ChatMessage;
import org.chat.mapper.ChatMessageMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootTest
class ChatApplicationTests {
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Test
    void contextLoads() {
//        List<ChatMessage> list = chatMessageMapper.list(1000);
//        System.out.println(list);

        ChatMessage chatMessage = ChatMessage.builder().build();
        chatMessage.setNickName("ccc");
        chatMessage.setMessage("mmmm");
        int insert = chatMessageMapper.insert(chatMessage);
        System.out.println(insert);
    }
}
