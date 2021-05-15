package com.generator.randomgeneratorgame.prstc;

import com.generator.randomgeneratorgame.prstc.model.Human;

import java.util.List;

public interface NumberHistoryService {

    void create(String number);

    List<String> getHistory();
}
