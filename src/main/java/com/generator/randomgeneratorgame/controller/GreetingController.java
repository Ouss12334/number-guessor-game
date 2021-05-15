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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GreetingController {

    private final HumanService humanService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public List<Greeting> greeting(User helloMessage, @Header("simpSessionId") String sessionId) {
        log.debug("session id {}", sessionId);
        humanService.create(HtmlUtils.htmlEscape(helloMessage.getName()), sessionId);
        return humanService.getNames().stream()
                .map(human -> new Greeting(human.getUsername(), human.getScore()))
                .collect(Collectors.toList());
    }
}
