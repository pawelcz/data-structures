package com.pczyz.social.domain.message

import com.pczyz.social.domain.user.User
import com.pczyz.social.domain.user.UserService
import com.pczyz.social.domain.util.TimeProvider
import com.pczyz.social.rest.MessageRequest
import spock.lang.Specification
import spock.lang.Subject

import static java.time.LocalDateTime.of

class MessageServiceSpec extends Specification {

    MessageRepository messageRepository = Mock()
    UserService userService = Mock()
    TimeProvider timeProvider = Mock()

    @Subject
    MessageService messageService = new MessageService(messageRepository, userService, timeProvider)

    def 'should post message'() {
        given:
        def user = new User('test@example.com', [] as Set)
        def messageContent = 'TestContent'
        def time = of(2020, 2, 1, 10, 20, 30)
        def request = new MessageRequest(user.email, messageContent)

        when:
        messageService.postMessage(request)

        then:
        1 * userService.getOrCreateUser(user.email, ) >> user
        1 * timeProvider.getCurrentTime() >> time
        1 * messageRepository.addMessageByUser(user, new Message(user, messageContent, time))
    }

    def 'should get messages by existing user'() {
        given:
        def user = new User('test@example.com', [] as Set)
        def messages = [
                new Message(user, 'FirstMessage', of(2020, 3, 2, 20, 30, 40)),
                new Message(user, 'SecondMessage', of(2020, 2, 1, 10, 20, 30))
        ]

        when:
        def result = messageService.getUserMessagesFromLatest(user.email)

        then:
        result == messages
        1 * userService.getUserOrThrowException(user.email) >> user
        1 * messageRepository.getAllUserMessagesSortedStream(user) >> messages
    }

    def 'should get empty list if user has no messages'() {
        given:
        def user = new User('test@example.com', [] as Set)

        when:
        def result = messageService.getUserMessagesFromLatest(user.email)

        then:
        result == []
        1 * userService.getUserOrThrowException(user.email) >> user
        1 * messageRepository.getAllUserMessagesSortedStream(user) >> []
    }

    def 'should get followed users messages'() {
        given:
        def followedUser1 = new User('followed1@example.com', [] as Set)
        def followedUser2 = new User('followed2@example.com', [] as Set)
        def followedUsers = [followedUser1, followedUser2] as Set
        def followedUsersEmails = [followedUser1.email, followedUser2.email] as Set
        def followingUser = new User('following@example.com', followedUsersEmails)
        def followedUsersMessages = [
                new Message(followedUser1, 'FirstMessage', of(2020, 3, 2, 20, 30, 40)),
                new Message(followedUser2, 'SecondMessage', of(2020, 2, 1, 10, 20, 30))
        ]

        when:
        def result = messageService.getFollowedUsersMessagesFromLatest(followingUser.email)

        then:
        result == followedUsersMessages
        1 * userService.getUserOrThrowException(followingUser.email) >> followingUser
        1 * userService.getFollowedUsers(followingUser) >> followedUsers
        1 * messageRepository.getAllUsersMessagesSorted(followedUsers) >> followedUsersMessages
    }
}
