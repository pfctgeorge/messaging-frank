package com.frank.messaging.request;

import lombok.Data;

@Data
public class ActivateUserRequest {
    private String username;
    private String validationCode;
}
