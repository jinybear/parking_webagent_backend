package Nuricon.parking_webagent_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Log {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String level;
    private String description;

    @Column(name = "src_ipaddress")
    private String srcIpaddress;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
