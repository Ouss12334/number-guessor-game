package com.generator.randomgeneratorgame.prstc;

import com.generator.randomgeneratorgame.prstc.model.Human;
import com.generator.randomgeneratorgame.prstc.repo.HumanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HumanServiceImpl implements HumanService {

    private final HumanRepository humanRepository;

    public void create(String name, String sessionId) {
        humanRepository.save(Human.builder().messageId(sessionId).username(name).build());
    }

    public String getUser(String sessionId) {
        return humanRepository.findByMessageId(sessionId).getUsername();
    }
}
