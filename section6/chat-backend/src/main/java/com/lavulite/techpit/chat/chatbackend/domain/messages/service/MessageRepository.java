package com.lavulite.techpit.chat.chatbackend.domain.messages.service;

import com.lavulite.techpit.chat.chatbackend.domain.messages.model.Message;

public interface MessageRepository {
  void insert(Message message);
}
