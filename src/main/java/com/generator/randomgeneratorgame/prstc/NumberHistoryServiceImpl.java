package com.generator.randomgeneratorgame.prstc;

import com.generator.randomgeneratorgame.prstc.model.NumberHistory;
import com.generator.randomgeneratorgame.prstc.repo.NumberHistoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NumberHistoryServiceImpl implements NumberHistoryService {

    private NumberHistoryRepo numberHistoryRepo;

    public void create(String number) {
        numberHistoryRepo.save(NumberHistory.builder().number(number).build());
    }

    public List<String> getHistory() {
        return numberHistoryRepo.findAll()
                .stream()
                .map(NumberHistory::getNumber)
                .collect(Collectors.toList());
    }
}
