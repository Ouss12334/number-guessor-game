package com.generator.randomgeneratorgame.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Greeting {

    private String content;

    private int score;
}
