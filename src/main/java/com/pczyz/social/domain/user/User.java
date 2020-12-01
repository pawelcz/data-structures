package com.pczyz.social.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"followedUsersEmails"})
public class User {
    private String email;
    private Set<String> followedUsersEmails;
}
