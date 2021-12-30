package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.EdgeSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EdgeSourceRepository extends JpaRepository<EdgeSource, Long> {
    List<EdgeSource> findBySourceId(int sourceId);
}
