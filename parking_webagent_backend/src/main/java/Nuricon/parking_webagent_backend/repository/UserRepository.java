package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserid(String id);
    @Query(nativeQuery = true, value="DELETE FROM User WHERE id IN :ids")
    void deleteAllByIdInQuery(@Param("ids") List<String> ids);

}
