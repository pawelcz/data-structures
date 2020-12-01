package com.pczyz.social.domain.message;

import com.pczyz.social.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class MessageRepository {

    private final Map<User, List<Message>> messages = new HashMap<>();

    void addMessageByUser(User user, Message message) {
        messages.putIfAbsent(user, new ArrayList<>());
        messages.get(user).add(message);
    }

    List<Message> getAllUserMessagesSortedStream(User user) {
        return getAllUserMessagesStream(user)
                .sorted(Comparator.comparing(Message::getCreateTime).reversed())
                .collect(toList());
    }

    List<Message> getAllUsersMessagesSorted(Set<User> users) {
        return users.stream()
                .flatMap(this::getAllUserMessagesStream)
                .sorted(Comparator.comparing(Message::getCreateTime).reversed())
                .collect(toList());
    }

    private Stream<Message> getAllUserMessagesStream(User user) {
        return messages.getOrDefault(user, new ArrayList<>()).stream();
    }

    public void clearData() {
        messages.clear();
    }

}
