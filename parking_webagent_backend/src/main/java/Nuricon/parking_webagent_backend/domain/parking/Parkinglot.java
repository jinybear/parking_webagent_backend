package Nuricon.parking_webagent_backend.domain.parking;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="parkinglots")
public class Parkinglot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parkinglot_id")
    private int parkinglotId;
    private String name;

    @Column(name = "area_id")
    private int areaId;

    public Parkinglot() {
    }

    public Parkinglot(int areaId, int parkinglotId, String password){
        this.areaId = areaId;
        this.parkinglotId = parkinglotId;
        this.name = name;
    }
}
