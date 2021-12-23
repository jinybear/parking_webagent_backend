package Nuricon.parking_webagent_backend.domain;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;

//@Builder : 객체생성시 편리성
//@NoArgsConstructor
//@AllArgsConstructor
@Data //겟,셋,투스트링 포함
@Document(collection = "sources")
public class Source {
    @Id //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId _id;
    @Field("area_id")
    private int areaId;
    private int source_id;
    private String source_desc;
}
