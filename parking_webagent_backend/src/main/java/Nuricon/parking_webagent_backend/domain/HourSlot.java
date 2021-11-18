package Nuricon.parking_webagent_backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("slot")
public class HourSlot {

    private int slot_id;
    private Double slot_full_rate;
    private int slot_utilization_count;
    private String activation_minute;
    private String front_utilization;
    private String back_utilization;
}