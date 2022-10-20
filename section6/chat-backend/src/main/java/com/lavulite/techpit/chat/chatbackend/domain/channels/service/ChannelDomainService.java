package com.lavulite.techpit.chat.chatbackend.domain.channels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.lavulite.techpit.chat.chatbackend.domain.channels.model.Channel;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChannelDomainService {

  private final ChannelRepository channelRepository;

  public Channel create(Channel channel) {
    // ユーザからは登録用のIDを受け取らない仕様のため、DB内にあるIDの最大値+1を新しいチャンネルのIDとする。
    var currentMaxId = channelRepository.getMaxId();
    var newid = currentMaxId.orElse(0) + 1;
    channel.setId(newid);

    // DBに永続化する。
    channelRepository.insert(channel);
    return channel;
  }

  public List<Channel> findAll() {
    return channelRepository.findAll();
  }

  public Channel update(Channel channle){
    channelRepository.update(channle);
    return channle;
  }
}
