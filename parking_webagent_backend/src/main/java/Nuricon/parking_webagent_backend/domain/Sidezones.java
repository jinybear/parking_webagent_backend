package Nuricon.parking_webagent_backend.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "sidezones")
public class Sidezones {
    @Field("sidezone_id")
    private int sidezoneId;
}
