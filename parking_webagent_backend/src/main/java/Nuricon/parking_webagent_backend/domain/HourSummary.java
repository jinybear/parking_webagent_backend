package Nuricon.parking_webagent_backend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document("hoursummaries")
public class HourSummary {
    @Id
    private String _id;
    private String area_id;
    private double area_full_rate;
    private int area_utilization_count;
    private double area_rotation_rate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime start_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime end_date;

    @DBRef(lazy = true)
    private List<HourSector> sector;

}
