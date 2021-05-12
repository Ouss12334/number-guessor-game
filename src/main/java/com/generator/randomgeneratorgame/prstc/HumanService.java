package com.generator.randomgeneratorgame.prstc;

import com.generator.randomgeneratorgame.prstc.repo.HumanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface HumanService {

    void create(String name, String sessionId);

    String getUser(String sessionId);
}
