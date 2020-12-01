package com.pczyz.social.domain.message;

import com.pczyz.social.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Message {
    private User user;
    private String content;
    private LocalDateTime createTime;
}
