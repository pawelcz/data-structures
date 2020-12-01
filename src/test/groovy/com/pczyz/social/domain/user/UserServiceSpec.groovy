package com.pczyz.social.domain.user

import spock.lang.Specification
import spock.lang.Subject

import java.util.stream.Stream

import static java.util.Optional.empty
import static java.util.Optional.of

class UserServiceSpec extends Specification {

    UserRepository userRepository = Mock()

    @Subject
    UserService userService = new UserService(userRepository)

    def 'should create user if not exists'() {
        given:
        def user = new User('test@example.com', [] as Set)

        when:
        def result = userService.getOrCreateUser(user.email)

        then:
        result == user
        1 * userRepository.getUserOrEmpty(user.email) >> empty()
        1 * userRepository.addUser(user)
    }

    def 'should get user if exists'() {
        given:
        def user = new User('test@example.com', [] as Set)

        when:
        def result = userService.getOrCreateUser(user.email)

        then:
        result == user
        1 * userRepository.getUserOrEmpty(user.email) >> of(user)
        0 * userRepository.addUser(user)
    }

    def 'should throw exception if user not exists'() {
        given:
        def user = new User('test@example.com', [] as Set)

        when:
        userService.getUserOrThrowException(user.email)

        then:
        def e = thrown(UserNotExistsException)
        e.message == "User [${user.email}] not exists"
        1 * userRepository.getUserOrEmpty(user.email) >> empty()
    }

    def 'should add followed user'() {
        given:
        def followingUser = new User('following@example.com', [] as Set)
        def followedUser = new User('followed@example.com', [] as Set)

        when:
        userService.followUser(followingUser.email, followedUser.email)

        then:
        1 * userRepository.getUserOrEmpty(followingUser.email) >> of(followingUser)
        1 * userRepository.getUserOrEmpty(followedUser.email) >> of(followedUser)
        1 * userRepository.addFollowedUser(followingUser, followedUser)
    }

    def 'should remove followed user'() {
        given:
        def followedUser = new User('followed@example.com', [] as Set)
        def followingUser = new User('following@example.com', [followedUser.email] as Set)

        when:
        userService.unfollowUser(followingUser.email, followedUser.email)

        then:
        1 * userRepository.getUserOrEmpty(followingUser.email) >> of(followingUser)
        1 * userRepository.getUserOrEmpty(followedUser.email) >> of(followedUser)
        1 * userRepository.getUsersByEmailsStream(followingUser.followedUsersEmails) >> Stream.of(followedUser)
        1 * userRepository.removeFollowedUser(followingUser, followedUser)
    }

    def 'should throw exception when trying to unfollow not followed user'() {
        given:
        def followingUser = new User('following@example.com', [] as Set)
        def followedUser = new User('followed@example.com', [] as Set)

        when:
        userService.unfollowUser(followingUser.email, followedUser.email)

        then:
        def e = thrown(UserNotFollowingException)
        e.message == "User [${followingUser.email}] is not following user [${followedUser.email}]"

        1 * userRepository.getUserOrEmpty(followingUser.email) >> of(followingUser)
        1 * userRepository.getUserOrEmpty(followedUser.email) >> of(followedUser)
        1 * userRepository.getUsersByEmailsStream(followingUser.followedUsersEmails) >> Stream.empty()
        0 * userRepository.removeFollowedUser(followingUser, followedUser)
    }

    def 'should throw exception when followed user not exists'() {
        given:
        def followingUser = new User('following@example.com', [] as Set)
        def followedUser = new User('followed@example.com', [] as Set)

        when:
        userService.followUser(followingUser.email, followedUser.email)

        then:
        def e = thrown(UserNotExistsException)
        e.message == "User [${followedUser.email}] not exists"

        1 * userRepository.getUserOrEmpty(followingUser.email) >> of(followingUser)
        1 * userRepository.getUserOrEmpty(followedUser.email) >> empty()
        0 * userRepository.addFollowedUser(followingUser, followedUser)
    }

    def 'should throw exception when following user not exists'() {
        given:
        def followingUser = new User('following@example.com', [] as Set)
        def followedUser = new User('followed@example.com', [] as Set)

        when:
        userService.followUser(followingUser.email, followedUser.email)

        then:
        def e = thrown(UserNotExistsException)
        e.message == "User [${followingUser.email}] not exists"

        1 * userRepository.getUserOrEmpty(followingUser.email) >> empty()
        0 * userRepository.getUserOrEmpty(followedUser.email)
        0 * userRepository.addFollowedUser(followingUser, followedUser)
    }

    def 'should get followed users'() {
        given:
        def followedUser1 = new User('followed1@example.com', [] as Set)
        def followedUser2 = new User('followed2@example.com', [] as Set)
        def followedUsersEmails = [followedUser1.email, followedUser2.email] as Set
        def followingUser = new User('following@example.com', followedUsersEmails)

        when:
        def results = userService.getFollowedUsersAlphabetical(followingUser.email)

        then:
        results == [followedUser1, followedUser2]
        1 * userRepository.getUserOrEmpty(followingUser.email) >> of(followingUser)
        1 * userRepository.getUsersByEmailsSorted(followingUser.followedUsersEmails) >> [followedUser1, followedUser2]
    }

    def 'should throw exception when getting followed users by non-existing user'() {
        given:
        def followingUser = new User('following@example.com', [] as Set)

        when:
        userService.getFollowedUsersAlphabetical(followingUser.email)

        then:
        def e = thrown(UserNotExistsException)
        e.message == "User [${followingUser.email}] not exists"
        1 * userRepository.getUserOrEmpty(followingUser.email) >> empty()
        0 * userRepository.getUsersByEmailsSorted(followingUser.followedUsersEmails)
    }
}
