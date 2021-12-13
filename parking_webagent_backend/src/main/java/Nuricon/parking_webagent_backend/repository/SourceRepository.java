package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.Source;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceRepository extends MongoRepository<Source, ObjectId> {
    List<Source> findByAreaId(int areaId);
    List<Source> findByAreaIdIn(List<Integer> areaList);
}