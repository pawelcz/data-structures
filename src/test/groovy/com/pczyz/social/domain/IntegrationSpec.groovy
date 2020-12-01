package com.pczyz.social.domain

import com.pczyz.social.SocialApplication
import com.pczyz.social.domain.message.MessageRepository
import com.pczyz.social.domain.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(classes = SocialApplication.class, webEnvironment = NONE)
class IntegrationSpec extends Specification {

    @Autowired
    MessageRepository messageRepository
    @Autowired
    UserRepository userRepository

    def cleanup() {
        messageRepository.clearData()
        userRepository.clearData()
    }
}
