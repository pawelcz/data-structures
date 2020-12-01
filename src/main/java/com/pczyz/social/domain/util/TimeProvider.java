package com.pczyz.social.domain.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeProvider {

    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

}
