package Nuricon.parking_webagent_backend.controller;

import Nuricon.parking_webagent_backend.domain.Log;
import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.repository.LogRepository;
import Nuricon.parking_webagent_backend.repository.RefreshTokenRepository;
import Nuricon.parking_webagent_backend.service.LogService;
import Nuricon.parking_webagent_backend.service.UserService;
import Nuricon.parking_webagent_backend.util.JwtUtil;
import Nuricon.parking_webagent_backend.util.enums.LogLevel;
import Nuricon.parking_webagent_backend.util.enums.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;
import java.util.*;
import java.time.Clock;



@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private LogService logService;


    @ApiOperation(value="This method is used to add account")
    @PostMapping("/api/user/add")
    public User account(@RequestBody UserForm form){
        User user = new User(form.getId(), form.getPassword(), Role.ROLE_ADMIN);
        this.userService.registUser(user);
        return user;
    }

    @ApiOperation(value="This method is used to get accounts")
    @PostMapping("/api/user/getAccountList")
    public List<User> getAccountList(){
        List<User> users=this.userService.getUserList();
        System.out.println(users);
        return users;
    }
    @ApiOperation(value="This method is used to delete accounts")
    @PostMapping("/api/user/deleteAccount")
    public void deleteAccount(@RequestBody Map<String, ArrayList<String>> params){
        List<String> ids= params.get("ids");
        System.out.println(ids);
        this.userService.deleteUser(ids);
    }

    @ApiOperation(value="This method is used to unlock accounts")
    @PostMapping("/api/user/unlockAccount")
    public void unlockAccount(@RequestBody Map<String, ArrayList<String>> params){
        List<String> ids= params.get("ids");
        System.out.println(ids);
        this.userService.unlock(ids);
    }

    @ApiOperation(value="This method is used to change password")
    @PostMapping("/api/user/changePassword")
    public void changePassword(@RequestBody UserForm user){
        String uuid=user.getId();
        String password = user.getPassword();
        this.userService.changePW(uuid, password);
    }

    @ApiOperation(value="This method is used to change password")
    @PostMapping("/api/user/changeMyPassword")
    public String changeMyPassword(HttpServletResponse response, @RequestBody Map<String, String> params)throws JsonProcessingException{
        System.out.println(params);
        String uuid=params.get("id");
        String nowpassword = params.get("nowpassword");
        String password = params.get("password");
        try{
            this.userService.changeMyPW(uuid, password, nowpassword);
        }catch(IllegalAccessException ex){
            response.setStatus(404);
            return ex.getMessage();
        }
        return null;
    }

    @PostMapping("/api/user/refresh")
    @ResponseBody
    public String refreshToken(HttpServletResponse response, @RequestBody TokenForm data) {
        try {
            
            refreshTokenRepository.findByToken(data.getToken());

            // 만약 refresh token 기간이 만료전으로 유효하면
            String userId = jwtUtil.extractName(data.getToken());
            Map<String, String> tokens = jwtUtil.generateTokens(userId, 1000*60*30);
            String token = new ObjectMapper().writeValueAsString(tokens);
            userService.updateRefreshToken(userId, tokens.get("refresh_token"));

            return token;

        } catch (NoSuchElementException ex){
            response.setStatus(404);
            return "Failed to process request";
        } catch (Exception ex) {
            response.setStatus(401);
            return "Failed to process request";
        }
    }

    @PostMapping("/api/user/login")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response, @RequestBody UserForm form) throws JsonProcessingException {
        String userId = form.getId();
        String password = form.getPassword();
        try{
            userService.checkIdAndPassword(userId, password);
        } catch(NoSuchElementException ex){
            response.setStatus(404);
            String notExistId = messageSource.getMessage("error.NotExistID", null, Locale.KOREA);
            // 로그인 실패
            logService.write(String.format("Login 실패 - [%s], 존재하지 않는 계정", userId), LogLevel.Information, request.getRemoteAddr());

            return notExistId;

        } catch(IllegalAccessException ex){
            response.setStatus(404);
            // 로그인 실패
            logService.write(String.format("Login 실패 - [%s], 해당계정 잠김 또는 패스워드 오류", userId), LogLevel.Information, request.getRemoteAddr());

            return ex.getMessage();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userId, form.getPassword())
        );

        Map<String, String> tokens = jwtUtil.generateTokens(userId, 1000 * 60 * 10);  // 10min
        String token = new ObjectMapper().writeValueAsString(tokens);
        userService.updateRefreshToken(userId, tokens.get("refresh_token"));

        // 로그인 성공
        logService.write(String.format("Login 성공 - [%s]", userId), LogLevel.Information, request.getRemoteAddr());

        return token;
    }

    @PostMapping("/api/user/logout")
    @ResponseBody
    public String logout(HttpServletRequest httpServletRequest) {
        // refreshToken 제거
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String userId = jwtUtil.extractName(token);
//            HttpSession session = httpServletRequest.getSession();
//            session.removeAttribute("access-token");
//            session.removeAttribute("request-token");

            userService.logout(userId);
        }

        return "OK";
    }
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class UserForm {
    private String id;
    private String password;
}

@Data
class TokenForm {
    private String token;
}
