package com.generator.randomgeneratorgame.controller;

import com.generator.randomgeneratorgame.model.Guess;
import com.generator.randomgeneratorgame.model.Match;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class NumberGeneratorController {

    private static String number;
    private static final String WIN = "TTTT";

    static {
        generateRandom();
    }

    private static void generateRandom() {
        Set<Long> numbers = new LinkedHashSet<>();
        while (numbers.size() < 4) {
            long nb = Math.round(Math.random() * 10);
            if (numbers.size() == 0 && nb != 0)
                numbers.add(nb);
            else if (!numbers.contains(nb))
                numbers.add(nb);
        }
        number = numbers.stream().map(Object::toString).collect(Collectors.joining(""));
        log.debug("generated number {}", number);
    }

    @MessageMapping("/guess")
    @SendTo("/topic/guess")
    public Match guess(Guess guess) {
        Match match = new Match();
        log.info("received number {}", guess.getNumber());
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
        }
        return match;
    }
}
