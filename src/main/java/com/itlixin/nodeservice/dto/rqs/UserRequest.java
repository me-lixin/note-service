package com.itlixin.nodeservice.dto.rqs;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String nickname;
}
