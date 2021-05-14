package com.generator.randomgeneratorgame.prstc;

import com.generator.randomgeneratorgame.prstc.model.Human;

import java.util.List;

public interface HumanService {

    void create(String name, String sessionId);

    void increaseScore(String sessionId);

    String getUser(String sessionId);

    List<Human> getNames();
}
