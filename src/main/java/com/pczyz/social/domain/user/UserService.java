package com.pczyz.social.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOrCreateUser(String email) {
        return getUserOrEmpty(email)
                .orElseGet(() ->
                        addUserAndGet(email)
                );
    }

    public User getUserOrThrowException(String email) {
        return userRepository.getUserOrEmpty(email)
                .orElseThrow(() -> new UserNotExistsException(email));
    }

    public void followUser(String followingEmail, String followedEmail) {
        User followingUser = getUserOrThrowException(followingEmail);
        User followedUser = getUserOrThrowException(followedEmail);
        userRepository.addFollowedUser(followingUser, followedUser);
    }

    public void unfollowUser(String followingEmail, String followedEmail) {
        User followingUser = getUserOrThrowException(followingEmail);
        User followedUser = getUserOrThrowException(followedEmail);
        checkIfUserIsFollowingUser(followingUser, followedUser);
        userRepository.removeFollowedUser(followingUser, followedUser);
    }

    public Set<User> getFollowedUsers(User user) {
        return userRepository.getUsersByEmailsStream(user.getFollowedUsersEmails())
                .collect(toSet());
    }

    public List<User> getFollowedUsersAlphabetical(String email) {
        User followingUser = getUserOrThrowException(email);
        return userRepository.getUsersByEmailsSorted(followingUser.getFollowedUsersEmails());
    }

    private void checkIfUserIsFollowingUser(User followingUser, User followedUser) {
        Set<User> followedUsers = getFollowedUsers(followingUser);
        if (!followedUsers.contains(followedUser)) {
            throw new UserNotFollowingException(followingUser.getEmail(), followedUser.getEmail());
        }
    }

    private Optional<User> getUserOrEmpty(String email) {
        return userRepository.getUserOrEmpty(email);
    }

    private User addUserAndGet(String email) {
        User newUser = new User(email, new HashSet<>());
        userRepository.addUser(newUser);
        return newUser;
    }
}
