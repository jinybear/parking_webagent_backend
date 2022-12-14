package Nuricon.parking_webagent_backend.service;

import Nuricon.parking_webagent_backend.domain.RefreshToken;
import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.repository.RefreshTokenRepository;
import Nuricon.parking_webagent_backend.repository.UserRepository;
import Nuricon.parking_webagent_backend.util.enums.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PasswordEncoder pe;

    private User getUser(String userId) throws NoSuchElementException{
        Optional<User> optionalUser = userRepo.findByUserid(userId);

        return optionalUser.get();
    }

    @Override
    public UserDetails loadUserByUsername(String id) {
        User user = getUser(id);

        User authAdmin = User.builder().id(user.getId())
                .userid(user.getUserid())
                .password(user.getPassword())
                .role(user.getRole())
                .failurecnt(0)
                .locked(false)
                .createdat(user.getCreatedat())
                .updatedat(user.getUpdatedat())
                .build();

        return authAdmin;
    }

    public void clearFailure(String userId){
        User user = getUser(userId);
        user.setFailurecnt(0);
    }

    public void unlock(String userId){
        User user = getUser(userId);
        user.setLocked(false);
    }

    public void registUser(User user){
        String encodedPassword = this.pe.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.saveAndFlush(user);
    }

    public void updateRefreshToken(String userId, String token) {
        User user = getUser(userId);
        RefreshToken rf = user.getRefreshToken();
        if (rf == null){
            // ?????? RefreshToken table ??? ?????? ??? user??? ???????????? ???.
            rf = new RefreshToken(token);
            refreshTokenRepository.saveAndFlush(rf);
            user.setRefreshToken(rf);
            //userRepo.save(user);
        } else {
            rf.setUpdatedAt(LocalDateTime.now());
            rf.setToken(token);
            //refreshTokenRepository.save(rf);
        }
    }

    public void logout(String userId){
        User user = getUser(userId);
        RefreshToken rf = user.getRefreshToken();
        if (rf != null) {
            rf.setToken("");
            rf.setUpdatedAt(LocalDateTime.now());
        }
    }

    // User??? failureCnt column??? ?????? ??????????????? ?????? ??? ???????????? locked column??? true??? ???????????? ??????(???????????? ??????????????? ???????????????)
    public void countFailure(String userId) {
        Optional<User> optionalUser = userRepo.findByUserid(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setFailurecnt(user.getFailurecnt() + 1);

            if(user.getFailurecnt() >= 3){
                user.setFailurecnt(0);
                user.setLocked(true);
            }
        }
    }

    // ?????? User ??? ???????????? ??? ?????? ??????
    public boolean getlocked(String userId){
        User user = getUser(userId);
        return user.isLocked();
    }

    // ???????????? ID, password??? ?????? DB??? ????????? password??? ??????????????? ?????? ??????
    public void checkIdAndPassword(String userId, String password) throws IllegalAccessException{
        if(getlocked(userId)) {
            String msg = messageSource.getMessage("error.Locked", null, Locale.getDefault());
            System.out.println(msg);
            throw new IllegalAccessException(msg);
        }

        UserDetails user= loadUserByUsername(userId);

        String encodedPW = pe.encode(password);
        System.out.println(user.getPassword());
        boolean passwordIsCorrect = pe.matches(password, user.getPassword());
        if (!passwordIsCorrect) {
            if(userRepo.findByUserid(userId).get().getRole().equals(Role.ROLE_SUPERADMIN)) {
                String msg = messageSource.getMessage("error.BadCredentials", null, LocaleContextHolder.getLocale());
                throw new IllegalAccessException(msg);
            }

            //?????? ?????????
            countFailure(userId);
            if (getlocked(userId)) {
                String failureCnt = messageSource.getMessage("error.FailureCnt", null, LocaleContextHolder.getLocale());
                throw new IllegalAccessException(failureCnt);
            } else {
                String msg = messageSource.getMessage("error.BadCredentials", null, LocaleContextHolder.getLocale());
                throw new IllegalAccessException(msg);
            }
        } else {
            clearFailure(userId);
        }
    }
}
