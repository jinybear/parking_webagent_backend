package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.OutStatistics;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OutStatisticsRepository extends MongoRepository<OutStatistics, ObjectId> {
    List<OutStatistics> findByAreaIdAndDateGreaterThanEqual(int areaId, LocalDateTime data);
}
