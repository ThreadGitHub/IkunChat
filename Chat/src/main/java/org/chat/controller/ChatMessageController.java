package org.chat.controller;

import lombok.AllArgsConstructor;
import org.chat.domain.entity.ChatMessage;
import org.chat.mapper.ChatMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author
 * @date 2023/9/15 14:50
 */
@RestController
@AllArgsConstructor
@RequestMapping("/chat-message")
public class ChatMessageController {
    private ChatMessageMapper chatMessageMapper;

//    @GetMapping("/insert")
//    public String insert() {
//        ChatMessage chatMessage = ChatMessage.builder().nickName("test").message("msg").time(new Date()).build();
//        return "update: " + chatMessageMapper.insert(chatMessage);
//    }

    @GetMapping("/list")
    public List<ChatMessage> list() {
        List<ChatMessage> list = chatMessageMapper.list();
        return list;
    }
}
