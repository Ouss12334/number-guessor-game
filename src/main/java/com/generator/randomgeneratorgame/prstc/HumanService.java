package com.generator.randomgeneratorgame.prstc;

import com.generator.randomgeneratorgame.prstc.repo.HumanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HumanService {

    void create(String name, String sessionId);

    String getUser(String sessionId);

    List<String> getSessions();
}
