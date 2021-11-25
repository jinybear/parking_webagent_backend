package Nuricon.parking_webagent_backend.repository;
import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.domain.parking.ParkingArea;
import static Nuricon.parking_webagent_backend.domain.parking.QParkingArea.parkingArea;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParkingCustomRepositoryImpl implements ParkingCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public ParkingCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<ParkingArea> findAllParkingInfo() {
        List<ParkingArea> res = jpaQueryFactory.selectFrom(parkingArea).fetch();
        return res;
    }
}
