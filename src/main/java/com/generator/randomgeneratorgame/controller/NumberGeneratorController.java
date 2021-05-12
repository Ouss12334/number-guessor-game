package com.generator.randomgeneratorgame.controller;

import com.generator.randomgeneratorgame.model.Guess;
import com.generator.randomgeneratorgame.model.Match;
import com.generator.randomgeneratorgame.prstc.HumanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.*;
import java.util.stream.Collectors;

import static com.generator.randomgeneratorgame.service.GeneratorService.generateRandom;
import static com.generator.randomgeneratorgame.service.GeneratorService.shuffleLetters;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NumberGeneratorController {

    private static String number;
    private static final String WIN = "TTTT";
    private final HumanService humanService;

    static {
        number = generateRandom();
    }

    @MessageMapping("/guess")
    @SendTo("/topic/guess")
    public Match guess(Guess guess, @Header("simpSessionId") String sessionId) {
        log.debug("session Id {} ", sessionId);
        Match match = new Match();
        log.debug("received number {}", guess.getNumber());
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
            generateRandom();
            match.setWinner(humanService.getUser(sessionId));
            match.setNumber(NumberGeneratorController.number);
        }
        else
            match.setCorrespondence(shuffleLetters(match.getCorrespondence()));
        return match;
    }

}
