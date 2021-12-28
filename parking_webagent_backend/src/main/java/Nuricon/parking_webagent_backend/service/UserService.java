package Nuricon.parking_webagent_backend.service;

import Nuricon.parking_webagent_backend.domain.RefreshToken;
import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.repository.RefreshTokenRepository;
import Nuricon.parking_webagent_backend.repository.UserRepository;
import Nuricon.parking_webagent_backend.util.enums.LogLevel;
import Nuricon.parking_webagent_backend.util.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Transactional
@Service
public class UserService implements UserDetailsService {
    public class AccountLockedException extends Exception  {
        public AccountLockedException(String message) {
            super(message);
        }
    }

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PasswordEncoder pe;
    @Autowired
    private LogService logService;

    public User getUser(String userId) throws NoSuchElementException {
        Optional<User> optionalUser = userRepo.findByUserid(userId);

        return optionalUser.get();
    }

    private User getUserFromId(long id) throws NoSuchElementException {
        Optional<User> optionalUser = userRepo.findById(id);

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

    public void clearFailure(String userId) {
        User user = getUser(userId);
        user.setFailurecnt(0);
    }

    public void unlock(List<String> ids) {
        for (int i = 0; i < ids.size(); i++) {
            System.out.println(ids.get(i));
            long uid = Long.parseLong(ids.get(i));
            User user = getUserFromId(uid);
            user.setLocked(false);

            logService.write(String.format("계정 잠금 해제 - [%s]", uid), LogLevel.Information, null);
        }
    }

    public void registUser(User user) {
        String encodedPassword = this.pe.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);
    }

    public List<User> getUserList() {
        return userRepo.findAll();
    }

    public void deleteUser(List<String> ids) {
        userRepo.deleteAllByIdInQuery(ids);
    }

    public void updateRefreshToken(String userId, String token) {
        User user = getUser(userId);
        RefreshToken rf = user.getRefreshToken();
        if (rf == null) {
            // 먼저 RefreshToken table 에 추가 후 user에 추가해야 함.
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

    public void logout(String userId) {
        User user = getUser(userId);
        RefreshToken rf = user.getRefreshToken();
        if (rf != null) {
            rf.setToken("");
            rf.setUpdatedAt(LocalDateTime.now());
        }
    }

    // User의 failureCnt column의 값을 증가시키고 특정 값 이상이면 locked column을 true로 전환하는 함수(실패횟수 카운트해서 잠금처리용)

    public void countFailure(String userId) {
        Optional<User> optionalUser = userRepo.findByUserid(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFailurecnt(user.getFailurecnt() + 1);

            if (user.getFailurecnt() >= 3) {
                user.setFailurecnt(0);
                user.setLocked(true);
            }
        }
    }

    // 해당 User 의 잠금상태 값 조회 함수
    public boolean getlocked(String userId) {
        User user = getUser(userId);
        return user.isLocked();
    }

    // 입력받은 ID, password에 대해 DB에 없거나 password가 틀린경우에 대한 처리
    public void checkIdAndPassword(String userId, String password) throws IllegalAccessException, AccountLockedException {
        if (getlocked(userId)) {
            String msg = messageSource.getMessage("error.Locked", null, Locale.getDefault());
            System.out.println(msg);
            throw new IllegalAccessException(msg);
        }

        UserDetails user = loadUserByUsername(userId);

        String encodedPW = pe.encode(password);
        System.out.println(user.getPassword());
        boolean passwordIsCorrect = pe.matches(password, user.getPassword());
        if (!passwordIsCorrect) {
            if (userRepo.findByUserid(userId).get().getRole().equals(Role.ROLE_SUPERADMIN)) {
                String msg = messageSource.getMessage("error.BadCredentials", null, LocaleContextHolder.getLocale());
                throw new IllegalAccessException(msg);
            }

            //실패 카운트
            countFailure(userId);

            if (getlocked(userId)) {
                String msg = messageSource.getMessage("error.FailureCnt", null, LocaleContextHolder.getLocale());
                throw new AccountLockedException(msg);
            } else {
                String msg = messageSource.getMessage("error.BadCredentials", null, LocaleContextHolder.getLocale());
                throw new IllegalAccessException(msg);
            }
        } else {
            clearFailure(userId);
        }
    }

    //비밀번호 변경 처리
    public void changePW(String uuid, String password) {
        long uid = Long.parseLong(uuid);
        User user = getUserFromId(uid);
        String encodedPW = pe.encode(password);
        user.setPassword(encodedPW);
        //dirty check 로 인한 update
    }

    public void changeMyPW(String uuid, String password, String nowpassword) throws IllegalAccessException {
        long uid = Long.parseLong(uuid);
        User user = getUserFromId(uid);
        boolean passwordIsCorrect = pe.matches(nowpassword,user.getPassword());
        if (!passwordIsCorrect) {
            String msg = messageSource.getMessage("error.NotCorrectPW", null, LocaleContextHolder.getLocale());
            throw new IllegalAccessException(msg);
        }else{
            user.setPassword(pe.encode(password));
        }
        //dirty check 로 인한 update

    }
}
