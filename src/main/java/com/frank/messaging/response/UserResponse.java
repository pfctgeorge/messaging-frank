package com.frank.messaging.response;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UserResponse {
    int id;
    String username;
    String nickname;
}
