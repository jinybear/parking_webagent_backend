package Nuricon.parking_webagent_backend.service;

import Nuricon.parking_webagent_backend.domain.HourSummary;
import Nuricon.parking_webagent_backend.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {
    private TestRepository testRepository;

    public EventService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public Optional<HourSummary> getTest(String id){
        return testRepository.findById(id);
    }
}
