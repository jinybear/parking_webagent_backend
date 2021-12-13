package Nuricon.parking_webagent_backend.domain;

import com.sun.istack.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.util.annotation.Nullable;

import javax.persistence.Id;
import java.util.Date;

@Data
@Document(collection = "statistics")
public class Statistics {
    @Id
    private ObjectId _id;
    @Field("area_id")
    private int areaId;
    @Field("sector_id")
    private int sectorId;
    @Field("slot_id")
    private int slotId;
    @Field("parking_status")
    private int parkingStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd 'T' HH:mm:ss")
    private Date date;
}
