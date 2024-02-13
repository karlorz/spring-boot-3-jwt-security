package com.alibou.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("email")
  private String email;
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("expired_at")
  private Date expiredAt;

  @JsonProperty("user")
  private User user;
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class User {
    @JsonProperty("email")
    private String email;
  }


}
