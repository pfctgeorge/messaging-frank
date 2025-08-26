package com.frank.messaging.dto;

import lombok.Data;

@Data
public class UserValidationCodeDTO {
    private int id;
    private int userId; // foreign key
    private String validationCode;
}
