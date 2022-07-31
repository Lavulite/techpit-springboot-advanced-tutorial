package com.lavulite.techpit.chat.chatbackend.app.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavulite.techpit.chat.chatbackend.domain.channels.model.Channel;

@RestController
@RequestMapping("/channel")
public class ChannelController {

  @PostMapping()
  public Channel create(@RequestBody Channel channel){
    // TODO: Serviceを作成するまでは暫定的にリクエスト内容をそのまま返却する。
    return channel;
  }

  // ↓↓↓ ここから追加 ↓↓↓
  @GetMapping()
  public List<Channel> findAll(){
    // TODO: Serviceを作成するまでは暫定的に空のリストを返却する。
    return Collections.emptyList();
  }
  // ↑↑↑ ここまで追加 ↑↑↑
}
