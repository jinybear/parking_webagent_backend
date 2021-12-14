package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.HourSummary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends MongoRepository<HourSummary, ObjectId> {
    List<HourSummary> findByAreaId(int area_id);
    List<HourSummary> findByAreaUtilizationCount(int count);
}

