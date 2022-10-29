package com.lavulite.techpit.chat.chatbackend.infrastructure.channels;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lavulite.techpit.chat.chatbackend.domain.channels.model.Channel;

@Mapper
public interface ChannelMapper {
  void insert(Channel channel);
  
  List<Channel> findAll();
  
  Optional<Integer> getMaxId();

  int update(Channel channel);

  int delete(@Param("id") int id);
}
