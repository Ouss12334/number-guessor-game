package com.generator.randomgeneratorgame.controller;

import com.generator.randomgeneratorgame.model.Greeting;
import com.generator.randomgeneratorgame.model.User;
import com.generator.randomgeneratorgame.prstc.HumanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GreetingController {

    private final HumanService humanService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(User helloMessage, @Header("simpSessionId") String sessionId) throws InterruptedException {
        log.debug("session id {}", sessionId);
        humanService.create(helloMessage.getName(), sessionId);
        return new Greeting("Hello " + HtmlUtils.htmlEscape(helloMessage.getName() + "!"));
    }
}
