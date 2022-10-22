package com.lavulite.techpit.chat.chatbackend.domain.messages.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Message {
  // メッセージID
  private String id;

  // チャンネルID
  private int channelId;

  // メッセージ本文
  private String text;

  // 投稿者
  private String username;

  // 投稿日時
  private LocalDateTime timestamp;
}
