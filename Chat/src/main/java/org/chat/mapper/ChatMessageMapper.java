package org.chat.mapper;

import org.chat.domain.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 消息表 mapper
 * @author thread
 * @date 2023/9/15 12:46
 */
@Component
public class ChatMessageMapper {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ChatMessage> list(Integer limit) {
        String sql = "select * from chat_message limit " + limit;
        List<ChatMessage> chatMessageList = jdbcTemplate.query(sql, (rs, rowNum) -> {
            ChatMessage chatMessage = ChatMessage.builder().build();
            long id = rs.getLong("id");
            chatMessage.setId(id);
            String nickName = rs.getString("nick_name");
            chatMessage.setNickName(nickName);
            String message = rs.getString("message");
            chatMessage.setMessage(message);
            Date time = rs.getDate("time");
            chatMessage.setTime(time);
            return chatMessage;
        });
        return chatMessageList;
    }

//    @Transactional(rollbackFor = Exception.class)
    public int insert(ChatMessage chatMessage) {
        String sql = "insert into chat_message(nick_name, message, time) values(?, ?, ?)";
        Date time = chatMessage.getTime();
        String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
        return jdbcTemplate.update(sql, chatMessage.getNickName(), chatMessage.getMessage(), timeStr);
    }

    public int insert(String nickName, String msg, Date time) {
        ChatMessage chatMessage = ChatMessage.builder().nickName(nickName).message(msg).time(time).build();
        return this.insert(chatMessage);
    }

    public int insert(String nickName, String msg) {
        return this.insert(nickName, msg, new Date());
    }
}
