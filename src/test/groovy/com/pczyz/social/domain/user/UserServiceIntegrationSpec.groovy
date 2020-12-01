package com.pczyz.social.domain.user


import com.pczyz.social.domain.IntegrationSpec
import com.pczyz.social.domain.message.MessageService
import com.pczyz.social.rest.MessageRequest
import org.springframework.beans.factory.annotation.Autowired

class UserServiceIntegrationSpec extends IntegrationSpec {

    @Autowired
    UserRepository userRepository
    @Autowired
    UserService userService
    @Autowired
    MessageService messageService

    def 'should follow user and unfollow user'() {
        given: 'three new users post messages'
        def user1 = new User('first@example.com', [] as Set)
        def user2 = new User('second@example.com', [] as Set)
        def user3 = new User('third@example.com', [] as Set)
        def user1Request = new MessageRequest(user1.email, 'User1MessageContent')
        def user2Request = new MessageRequest(user2.email, 'User2MessageContent')
        def user3Request = new MessageRequest(user3.email, 'User3MessageContent')
        messageService.postMessage(user1Request)
        messageService.postMessage(user2Request)
        messageService.postMessage(user3Request)

        and: 'first user follow second and third users'
        userService.followUser(user1.email, user2.email)
        userService.followUser(user1.email, user3.email)

        when: 'first user get following users'
        def result = userService.getFollowedUsersAlphabetical(user1.email)

        then:
        result == [user2, user3]

        when: 'first user unfollow second user'
        userService.unfollowUser(user1.email, user2.email)

        and: 'first user get following users'
        result = userService.getFollowedUsersAlphabetical(user1.email)

        then:
        result == [user3]

        when: 'first user unfollow third user'
        userService.unfollowUser(user1.email, user3.email)

        and: 'first user get following users'
        result = userService.getFollowedUsersAlphabetical(user1.email)

        then:
        result == []

        when: 'first user try to unfollow third user again'
        userService.unfollowUser(user1.email, user3.email)

        then: 'exception is thrown'
        def e = thrown(UserNotFollowingException)
        e.message == "User [${user1.email}] is not following user [${user3.email}]"
    }

}
