package com.generator.randomgeneratorgame.prstc;

import com.generator.randomgeneratorgame.prstc.model.Human;
import com.generator.randomgeneratorgame.prstc.repo.HumanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HumanServiceImpl implements HumanService {

    private final HumanRepository humanRepository;

    public void create(String name, String sessionId) {
        humanRepository.save(Human.builder().messageId(sessionId).username(name).score(0).build());
    }

    public void increaseScore(String sessionId) {
        Human human = humanRepository.findByMessageId(sessionId);
        humanRepository.save(human.withScore(human.getScore() + 1));
    }

    public String getUser(String sessionId) {
        return humanRepository.findByMessageId(sessionId).getUsername();
    }

    public List<Human> getNames() {
        return humanRepository.findAll();
    }
}
