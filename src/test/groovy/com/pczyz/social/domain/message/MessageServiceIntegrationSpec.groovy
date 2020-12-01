package com.pczyz.social.domain.message


import com.pczyz.social.domain.IntegrationSpec
import com.pczyz.social.domain.user.User
import com.pczyz.social.domain.user.UserNotExistsException
import com.pczyz.social.domain.user.UserService
import com.pczyz.social.domain.util.TimeProvider
import com.pczyz.social.rest.MessageRequest
import org.springframework.beans.factory.annotation.Autowired

class MessageServiceIntegrationSpec extends IntegrationSpec {

    @Autowired
    MessageRepository messageRepository
    @Autowired
    TimeProvider timeProvider
    @Autowired
    MessageService messageService
    @Autowired
    UserService userService

    def 'should post messages and read messages from latest by new and existing users'() {
        given: 'new user post message'
        def user = new User('first@example.com', [] as Set)
        def firstUserFirstMessageContent = 'FirstUserFirstMessageContent'
        def request = new MessageRequest(user.email, firstUserFirstMessageContent)

        when:
        messageService.postMessage(request)
        def result = messageService.getUserMessagesFromLatest(user.email)

        then:
        result.size() == 1
        result.first().user == user
        result.first().content == firstUserFirstMessageContent

        when: 'the same user post message'
        def firstUserSecondMessageContent = 'FirstUserSecondMessageContent'
        request.messageContent = firstUserSecondMessageContent
        messageService.postMessage(request)
        result = messageService.getUserMessagesFromLatest(user.email)

        then:
        result.size() == 2
        result[0].user == user
        result[1].user == user
        result[0].content == firstUserSecondMessageContent
        result[1].content == firstUserFirstMessageContent
        result[0].createTime.isAfter(result[1].createTime)

        when: 'second, non existing user post message'
        def secondUser = new User('second@example.com', [] as Set)
        def secondUserFirstMessageContent = 'SecondUserFirstMessageContent'
        request.userEmail = secondUser.email
        request.messageContent = secondUserFirstMessageContent

        messageService.postMessage(request)
        result = messageService.getUserMessagesFromLatest(secondUser.email)

        then:
        result.size() == 1
        result.first().user == secondUser
        result.first().content == secondUserFirstMessageContent
    }

    def 'should get messages from followed users'() {
        given: 'three new users post messages'
        def user1 = new User('first@example.com', [] as Set)
        def user2 = new User('second@example.com', [] as Set)
        def user3 = new User('third@example.com', [] as Set)
        def user1Request = new MessageRequest(user1.email, 'User1MessageContent')
        def user2Request = new MessageRequest(user2.email, 'User2MessageContent')
        def user3Request = new MessageRequest(user3.email, 'User3MessageContent')
        messageService.postMessage(user1Request)
        messageService.postMessage(user2Request)
        sleep(100)
        messageService.postMessage(user3Request)

        and: 'first user follow second and third users'
        userService.followUser(user1.email, user2.email)
        userService.followUser(user1.email, user3.email)

        when: 'first user gets followed users messages'
        def result = messageService.getFollowedUsersMessagesFromLatest(user1.email)

        then:
        result.size() == 2
        result[0].user == user3
        result[1].user == user2
        result[0].content == user3Request.messageContent
        result[1].content == user2Request.messageContent
        result[0].createTime.isAfter(result[1].createTime)
    }

    def 'should throw exception when getting messages by non-existing user'() {
        given:
        def nonExistingUserEmail = 'non-existing@example.com'

        when:
        messageService.getUserMessagesFromLatest(nonExistingUserEmail)

        then:
        def e = thrown(UserNotExistsException)
        e.message == "User [${nonExistingUserEmail}] not exists"
    }

    def 'should throw exception when getting followed users messages by non-existing user'() {
        given:
        def nonExistingUserEmail = 'non-existing@example.com'

        when:
        messageService.getFollowedUsersMessagesFromLatest(nonExistingUserEmail)

        then:
        def e = thrown(UserNotExistsException)
        e.message == "User [${nonExistingUserEmail}] not exists"
    }


}
