package com.pczyz.social.rest;

import com.pczyz.social.domain.message.Message;
import com.pczyz.social.domain.message.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    void postMessage(@RequestBody @Valid MessageRequest request) {
        log.info("Posting new message by user [{}]", request.getUserEmail());
        messageService.postMessage(request);
    }

    @GetMapping
    List<Message> getUserMessages(@RequestParam String userEmail) {
        log.info("Getting all messages by user [{}]", userEmail);
        return messageService.getUserMessagesFromLatest(userEmail);
    }

    @GetMapping("/followed")
    List<Message> getFollowedUsersMessages(@RequestParam String userEmail) {
        log.info("Getting followed users messages by user [{}]", userEmail);
        return messageService.getFollowedUsersMessagesFromLatest(userEmail);
    }

}
