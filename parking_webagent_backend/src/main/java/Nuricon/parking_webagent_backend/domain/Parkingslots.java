package Nuricon.parking_webagent_backend.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Data
@Document(collection = "parkingslots")
public class Parkingslots {
    @Id
    private ObjectId _id;
    @Field("area_id")
    private int areaId;
}
