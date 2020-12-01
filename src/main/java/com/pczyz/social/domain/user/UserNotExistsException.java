package com.pczyz.social.domain.user;

import static java.lang.String.format;

public class UserNotExistsException extends RuntimeException {

    public UserNotExistsException(String email) {
        super(format("User [%s] not exists", email));
    }
}
