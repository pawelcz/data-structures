package com.pczyz.social.domain.user;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class UserRepository {

    private final Set<User> users = new HashSet<>();

    Optional<User> getUserOrEmpty(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    Stream<User> getUsersByEmailsStream(Set<String> emails) {
        return users.stream()
                .filter(user -> emails.contains(user.getEmail()));
    }

    List<User> getUsersByEmailsSorted(Set<String> emails) {
        return getUsersByEmailsStream(emails)
                .sorted(Comparator.comparing(User::getEmail))
                .collect(toList());
    }

    void addUser(User user) {
        users.add(user);
    }

    void addFollowedUser(User followingUser, User followedUser) {
        followingUser.getFollowedUsersEmails().add(followedUser.getEmail());
    }

    void removeFollowedUser(User followingUser, User followedUser) {
        followingUser.getFollowedUsersEmails().remove(followedUser.getEmail());
    }

    public void clearData() {
        users.clear();
    }

}
