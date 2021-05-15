package com.generator.randomgeneratorgame.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public interface GeneratorService {

    Logger log = LoggerFactory.getLogger(GeneratorService.class);

    static String shuffleLetters(String match) {
        List<String> letters = Arrays.asList(match.split(""));
        Collections.shuffle(letters);
        StringBuilder builder = new StringBuilder();
        for (String letter : letters) {
            builder.append(letter);
        }
        log.debug("shuffled {} to {}", match, builder.toString());
        return builder.toString();
    }

    static String generateRandom() {
        String number = "";
        Set<Long> numbers = new LinkedHashSet<>();
        while (numbers.size() < 4) {
            long nb = Math.round(Math.random() * 9);
            if (numbers.isEmpty() && nb != 0) // not start with 0
                numbers.add(nb);
            else if (numbers.size() > 0 && !numbers.contains(nb))
                numbers.add(nb);
        }
        number = numbers.stream().map(Object::toString).collect(Collectors.joining(""));
        log.info("generated number {}", number);
        return number;
    }

}
