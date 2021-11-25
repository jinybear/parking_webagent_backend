package Nuricon.parking_webagent_backend.domain.parking;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="parkingsectors")
public class ParkingSector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sector_id")
    private int sectorId;
    private String name;

    @Column(name = "parkinglot_id")
    private int parkinglotId;

    public ParkingSector() {
    }

    public ParkingSector(int sectorId, int parkinglotId, String password){
        this.sectorId = sectorId;
        this.parkinglotId = parkinglotId;
        this.name = name;
    }
}
