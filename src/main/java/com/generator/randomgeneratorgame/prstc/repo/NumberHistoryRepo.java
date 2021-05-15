package com.generator.randomgeneratorgame.prstc.repo;

import com.generator.randomgeneratorgame.prstc.model.NumberHistory;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberHistoryRepo extends JpaRepository<NumberHistory, String> {

}
