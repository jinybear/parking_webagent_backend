package Nuricon.parking_webagent_backend.service;

import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.repository.UserRepository;
import Nuricon.parking_webagent_backend.util.enums.Role;
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
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;
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

    // User의 failureCnt column의 값을 증가시키고 특정 값 이상이면 locked column을 true로 전환하는 함수(실패횟수 카운트해서 잠금처리용)
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

    // 해당 User 의 잠금상태 값 조회 함수
    public boolean getlocked(String userId){
        User user = getUser(userId);
        return user.isLocked();
    }

    // 입력받은 ID, password에 대해 DB에 없거나 password가 틀린경우에 대한 처리
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

            //실패 카운트
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
