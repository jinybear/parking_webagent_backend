package Nuricon.parking_webagent_backend.domain.parking;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="parkingareas")
public class ParkingArea {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "area_id")
    private int areaId;
    private String name;

    public ParkingArea() {
    }

    public ParkingArea(int areaId, String name){
        this.areaId = areaId;
        this.name = name;
    }
}


