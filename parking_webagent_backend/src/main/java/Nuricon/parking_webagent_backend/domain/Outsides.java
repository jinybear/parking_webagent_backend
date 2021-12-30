package Nuricon.parking_webagent_backend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.bson.types.ObjectId;
import org.hibernate.annotations.Filter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "outsides")
public class Outsides {
    @Id
    private ObjectId _id;
    @Field("source_id")
    private String sourceId;
    @Field("sidezones")
    private List<Sidezones> sidezones;
}
