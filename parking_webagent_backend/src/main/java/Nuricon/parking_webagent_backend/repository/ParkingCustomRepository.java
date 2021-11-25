package Nuricon.parking_webagent_backend.repository;

import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.domain.parking.ParkingArea;

import java.util.List;
import java.util.Optional;

public interface ParkingCustomRepository {
    List<ParkingArea> findAllParkingInfo();
}
