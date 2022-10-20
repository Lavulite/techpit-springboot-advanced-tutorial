package com.lavulite.techpit.chat.chatbackend.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lavulite.techpit.chat.chatbackend.domain.messages.model.Message;
import com.lavulite.techpit.chat.chatbackend.domain.messages.service.MessageDomainService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
  
  private final MessageDomainService messageDomainService;

  public Message post(Message message){
    // TODO: 8章で認証情報から取得するように修正する。
    var username = "guest";
    message.setUsername(username);
    return messageDomainService.post(message);
  }
}
