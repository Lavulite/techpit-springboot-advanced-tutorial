package com.lavulite.techpit.chat.chatbackend.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lavulite.techpit.chat.chatbackend.app.service.MessageService;
import com.lavulite.techpit.chat.chatbackend.domain.messages.model.Message;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/channels/{channelId}/messages")
@RequiredArgsConstructor
@CrossOrigin
public class MessageController {

  private final MessageService messageService;

  @PostMapping
  public Message post(@PathVariable int channelId, @RequestBody Message message) {
    message.setChannelId(channelId);
    return messageService.post(message);
  }

  @GetMapping
  public List<Message> find(
      @PathVariable("channelId") int channelId,
      @RequestParam("searchWord") Optional<String> searchWord) {
    return messageService.find(channelId, searchWord);
  }
}
