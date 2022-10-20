package com.lavulite.techpit.chat.chatbackend.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavulite.techpit.chat.chatbackend.app.service.ChannelService;
import com.lavulite.techpit.chat.chatbackend.domain.channels.model.Channel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
@CrossOrigin
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping()
  public Channel create(@RequestBody Channel channel){
    return channelService.create(channel);
  }

  @GetMapping()
  public List<Channel> findAll(){
    return channelService.findAll();
  }

  @PutMapping("/{id}")
  public Channel updade(@PathVariable("id") int id, @RequestBody Channel channel){
    channel.setId(id);
    return channelService.updade(channel);
  }
}
