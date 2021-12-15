package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends PagingAndSortingRepository<Log, Long> {
    Page<Log> findAll(Pageable pageable);
}
