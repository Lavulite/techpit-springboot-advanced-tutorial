package com.lavulite.techpit.chat.chatbackend.domain.channels.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Channel {
  // チャンネルID
  private int id;

  // チャンネル名
  private String name;  
}
