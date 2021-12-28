package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.Outsides;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OutsidesRepository extends MongoRepository<Outsides, ObjectId> {
    List<Outsides> findBySourceId(int sourceId);
}
