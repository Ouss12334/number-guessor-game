package com.generator.randomgeneratorgame.controller;

import com.generator.randomgeneratorgame.model.Greeting;
import com.generator.randomgeneratorgame.model.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(User helloMessage) throws InterruptedException {
        Thread.sleep(1000);
        return new Greeting("Hello " + HtmlUtils.htmlEscape(helloMessage.getName() + "!"));
    }
}
