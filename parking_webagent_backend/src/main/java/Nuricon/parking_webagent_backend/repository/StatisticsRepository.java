package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.Statistics;
import com.sun.istack.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.util.annotation.Nullable;

import java.util.List;

@Repository
public interface StatisticsRepository extends MongoRepository<Statistics, ObjectId> {
    //@Query("find({area_id:'areaId'}).sort({date:-1}).limit(100)")
    //List<Statistics> findByAreaId(int areaId);
    //List<Statistics> findTopByOrderByDatedDesc(int areaId);
    //가장 최신 데이터 가져올 애 하나
    Statistics findTopOneByAreaIdOrderByDateDesc(int areaId);
    List<Statistics> findTop300ByAreaIdOrderByDateDesc(int areaId);

}
