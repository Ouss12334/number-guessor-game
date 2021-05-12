package com.generator.randomgeneratorgame.prstc.repo;

import com.generator.randomgeneratorgame.prstc.model.Human;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumanRepository extends CrudRepository<Human, String> {

    Human findByMessageId(String messageId);
}
