package Nuricon.parking_webagent_backend.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("sector")
public class HourSector {
    private int sector_id;
    protected double sector_full_rate;
    protected int sector_utilization_count;
    protected double sector_rotation_rate;

    @DBRef(lazy = true)
    private List<HourSlot> slot;

}
