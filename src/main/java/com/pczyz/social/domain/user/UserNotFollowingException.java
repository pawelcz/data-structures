package com.pczyz.social.domain.user;

import static java.lang.String.format;

public class UserNotFollowingException extends RuntimeException {

    public UserNotFollowingException(String followingEmail, String followedEmail) {
        super(format("User [%s] is not following user [%s]", followingEmail, followedEmail));
    }
}
