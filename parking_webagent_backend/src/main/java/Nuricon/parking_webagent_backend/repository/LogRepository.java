package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
}
