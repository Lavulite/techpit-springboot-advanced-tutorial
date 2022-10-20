package com.lavulite.techpit.chat.chatbackend.infrastructure.messages;

import org.apache.ibatis.annotations.Mapper;

import com.lavulite.techpit.chat.chatbackend.domain.messages.model.Message;

@Mapper
public interface MessageMapper {
  void insert(Message message);
}
