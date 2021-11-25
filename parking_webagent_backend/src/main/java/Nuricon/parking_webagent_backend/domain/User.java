package Nuricon.parking_webagent_backend.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Column(name = "user_id")
    private String userId;

    public User() {
    }

    public User(String name, String password){
        this.password = password;
        this.userId = name;
    }
}

