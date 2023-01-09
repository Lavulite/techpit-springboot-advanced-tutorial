package com.lavulite.techpit.chat.chatbackend.app.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lavulite.techpit.chat.chatbackend.domain.hello.model.Hello;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@Slf4j
public class HelloController {
  
  @GetMapping("/hello")
  public Hello hello(@RequestParam("name") Optional<String> name) {
    String resName = name.orElse("world!");
    log.info(resName);
    log.warn(resName);
    log.error(resName);
    return new Hello("Hello, " + resName);
  }

}
