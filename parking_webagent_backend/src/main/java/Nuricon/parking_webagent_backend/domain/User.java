package Nuricon.parking_webagent_backend.domain;

import Nuricon.parking_webagent_backend.util.enums.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user")
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String password;

    private String userid;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(updatable = false)
    private LocalDateTime createdat;

    private LocalDateTime updatedat;
    private Integer failurecnt;
    private boolean locked;

    @OneToOne
    @JoinColumn(name="refreshtoken_id")
    private RefreshToken refreshToken;

    public User(String userid, String password, Role role){
        this.password = password;
        this.userid = userid;
        this.role = role;
        this.createdat = LocalDateTime.now();
        this.failurecnt = 0;
        this.locked = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for(String role: role.toString().split(",")) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return userid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

