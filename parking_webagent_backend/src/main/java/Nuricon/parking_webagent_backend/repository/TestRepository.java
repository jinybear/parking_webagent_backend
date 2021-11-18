package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.HourSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends MongoRepository<HourSummary, String> {
    @Override
    Optional<HourSummary> findById(String s);
}

