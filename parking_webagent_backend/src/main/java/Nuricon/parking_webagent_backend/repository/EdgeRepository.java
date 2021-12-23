package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, Long> {

}
