package com.lavulite.techpit.chat.chatbackend.app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavulite.techpit.chat.chatbackend.app.service.MessageService;
import com.lavulite.techpit.chat.chatbackend.domain.messages.model.Message;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@CrossOrigin
public class MessageController {
  
  private final MessageService messageService;

  @PostMapping
  public Message post(@RequestBody Message message) {
    return messageService.post(message);
  }
  
}
