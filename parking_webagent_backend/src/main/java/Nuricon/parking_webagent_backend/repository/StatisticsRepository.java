package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.Statistics;
import com.sun.istack.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface StatisticsRepository extends MongoRepository<Statistics, ObjectId> {
    List<Statistics> findByAreaIdAndDateGreaterThanEqual(int areaId, LocalDateTime date);
}
