package com.generator.randomgeneratorgame.controller;

import com.generator.randomgeneratorgame.model.Greeting;
import com.generator.randomgeneratorgame.model.Guess;
import com.generator.randomgeneratorgame.model.Match;
import com.generator.randomgeneratorgame.model.User;
import com.generator.randomgeneratorgame.prstc.HumanService;
import com.generator.randomgeneratorgame.prstc.NumberHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

import static com.generator.randomgeneratorgame.service.GeneratorService.generateRandom;
import static com.generator.randomgeneratorgame.service.GeneratorService.shuffleLetters;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NumberGeneratorController {

    private static final String WIN = "TTTT";
    private static String number;

    static {
        number = generateRandom();
    }

    private final HumanService humanService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NumberHistoryService numberHistoryService;

    @MessageMapping("/guess")
    @SendTo("/topic/guess")
    public Match guess(Guess guess, @Header("simpSessionId") String sessionId) {
        log.debug("session Id {}", sessionId);
        log.debug("received number {}", guess.getNumber());
        Match match = new Match();
        String noMatch = "0";
        match.setCorrespondence(noMatch);
        char[] number = guess.getNumber().toCharArray();
        for (int pos = 0; pos < number.length; pos++) {
            if (NumberGeneratorController.number.indexOf(number[pos]) != -1) {
                String found = "V";
                if (NumberGeneratorController.number.indexOf(number[pos]) == pos) {
                    found = "T";
                }
                // clear if default
                if (match.getCorrespondence().equals(noMatch)) {
                    match.setCorrespondence("");
                }
                match.setCorrespondence(match.getCorrespondence().concat(found));
            }
        }
        // new number
        if (match.getCorrespondence().equals(WIN)) {
            humanService.increaseScore(sessionId);
            match.setNumber(NumberGeneratorController.number);
            // add to history
            numberHistoryService.create(NumberGeneratorController.number);
            // gen new
            NumberGeneratorController.number = generateRandom();
            // send update
            simpMessagingTemplate.convertAndSend("/topic/history", numberHistoryService.getHistory());
        } else
            match.setCorrespondence(shuffleLetters(match.getCorrespondence()));
        // get name
        match.setUsername(humanService.getUser(sessionId));
        match.setSessionId(sessionId);
        // trigger sockets
        simpMessagingTemplate.convertAndSend("/topic/greetings", getScores());
        return match;
    }

    private List<Greeting> getScores() {
        return humanService.getNames().stream()
                .map(human -> new Greeting(human.getUsername(), human.getScore()))
                .collect(Collectors.toList());
    }

}
