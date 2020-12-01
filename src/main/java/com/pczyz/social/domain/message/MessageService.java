package com.pczyz.social.domain.message;

import com.pczyz.social.rest.MessageRequest;
import com.pczyz.social.domain.util.TimeProvider;
import com.pczyz.social.domain.user.UserService;
import com.pczyz.social.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final TimeProvider timeProvider;

    public void postMessage(MessageRequest request) {
        User user = userService.getOrCreateUser(request.getUserEmail());
        Message message = new Message(user, request.getMessageContent(), timeProvider.getCurrentTime());
        messageRepository.addMessageByUser(user, message);
    }

    public List<Message> getUserMessagesFromLatest(String email) {
        User user = userService.getUserOrThrowException(email);
        return messageRepository.getAllUserMessagesSortedStream(user);
    }

    public List<Message> getFollowedUsersMessagesFromLatest(String email) {
        User user = userService.getUserOrThrowException(email);
        Set<User> followedUsers = userService.getFollowedUsers(user);
        return messageRepository.getAllUsersMessagesSorted(followedUsers);
    }
}
