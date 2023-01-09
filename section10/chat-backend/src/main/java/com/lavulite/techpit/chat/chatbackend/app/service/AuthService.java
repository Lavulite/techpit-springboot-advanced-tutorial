package com.lavulite.techpit.chat.chatbackend.app.service;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lavulite.techpit.chat.chatbackend.domain.auth.model.SigninUser;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final JwtEncoder encoder;
  private final UserDetailsManager users;
  private final PasswordEncoder passwordEncoder;

  public String issueToken(Authentication authentication) {
    Instant now = Instant.now();
    
    // @formatter:off
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
        // トークンの有効期限を1時間とする
				.expiresAt(now.plusSeconds(36000L))
				.subject(authentication.getName())
				.build();
		// @formatter:on
    return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  public void addUser(SigninUser signinUser) {
    UserDetails user = User.builder()
        .username(signinUser.getUsername())
        .password(passwordEncoder.encode(signinUser.getPassword()))
        .roles("USER")
        .build();
    this.users.createUser(user);
  }
}
