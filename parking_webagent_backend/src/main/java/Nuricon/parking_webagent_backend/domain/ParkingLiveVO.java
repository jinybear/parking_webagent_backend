package Nuricon.parking_webagent_backend.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLiveVO {
    private int id;
    private long parkingFull;
    private long parkingEmpty;
    private long parkingTotal;
    private double fullPercent;


}
