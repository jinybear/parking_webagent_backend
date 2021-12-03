package Nuricon.parking_webagent_backend.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="refreshtoken")
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    public RefreshToken(String token){
        this.token = token;
    }
}
