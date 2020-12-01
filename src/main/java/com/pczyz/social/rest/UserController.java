package com.pczyz.social.rest;

import com.pczyz.social.domain.user.User;
import com.pczyz.social.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/follow")
    void followUser(@RequestParam String followingEmail, @RequestParam String followedEmail) {
        log.info("Adding user [{}] to following users by user [{}]", followedEmail, followingEmail);
        userService.followUser(followingEmail, followedEmail);
    }

    @DeleteMapping("/follow")
    void unfollowUser(@RequestParam String followingEmail, @RequestParam String followedEmail) {
        log.info("Removing user [{}] from following users by user [{}]", followedEmail, followingEmail);
        userService.unfollowUser(followingEmail, followedEmail);
    }

    @GetMapping("/follow")
    List<User> getFollowedUsers(@RequestParam String email) {
        log.info("Getting followed users by user [{}]", email);
        return userService.getFollowedUsersAlphabetical(email);
    }
}
