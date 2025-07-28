package com.ms_fisio.account.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String profilePhoto;
}
