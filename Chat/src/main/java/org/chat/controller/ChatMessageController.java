package org.chat.controller;

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
@RequestMapping("/chat-message")
public class ChatMessageController {
    @Autowired
    private ChatMessageMapper chatMessageMapper;

//    @GetMapping("/insert")
//    public String insert() {
//        ChatMessage chatMessage = ChatMessage.builder().nickName("test").message("msg").time(new Date()).build();
//        return "update: " + chatMessageMapper.insert(chatMessage);
//    }

    @GetMapping("/list")
    public List<ChatMessage> list() {
        List<ChatMessage> list = chatMessageMapper.list(Integer.MAX_VALUE);
        return list;
    }
}
