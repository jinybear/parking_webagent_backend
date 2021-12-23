package Nuricon.parking_webagent_backend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Document(collection = "outstatistics")
public class OutStatistics {
    @Id
    private ObjectId _id;
    @Field("area_id")
    private int areaId;
    @Field("source_id")
    private int sourceId;
    @Field("parking_count")
    private int parkingCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime date;
}
