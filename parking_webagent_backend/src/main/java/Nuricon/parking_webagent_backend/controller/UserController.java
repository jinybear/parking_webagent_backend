package Nuricon.parking_webagent_backend.controller;

import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.service.UserService;
import Nuricon.parking_webagent_backend.util.JwtUtil;
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
import java.time.Clock;
import java.util.*;

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


    @ApiOperation(value="This method is used to add account")
    @PostMapping("/user/add")
    public User account(@RequestBody UserForm form){
        User user = new User(form.getId(), form.getPassword(), Role.ROLE_ADMIN);
        this.userService.registUser(user);
        return user;
    }

    @ApiOperation(value="This method is used to get accounts")
    @PostMapping("/user/getAccountList")
    public List<User> getAccountList(){
        List<User> users=this.userService.getUserList();
        System.out.println(users);
        return users;
    }
    @ApiOperation(value="This method is used to delete accounts")
    @PostMapping("/user/deleteAccount")
    public void deleteAccount(@RequestBody Map<String, Object> params){
        List<String> ids= (List<String>) params.get("ids");
        System.out.println(ids);
        this.userService.deleteUser(ids);
    }
    @PostMapping("/user/refresh")
    @ResponseBody
    public String refreshToken(HttpServletResponse response, @RequestBody TokenForm data) throws JsonProcessingException {
        try {
            // 만약 refresh token 기간이 만료전으로 유효하면
            String userId = jwtUtil.extractName(data.getToken());
            Map<String, String> tokens = jwtUtil.generateTokens(userId, 1000*60*30);
            String token = new ObjectMapper().writeValueAsString(tokens);
            userService.updateRefreshToken(userId, tokens.get("refresh-token"));

            return token;

        } catch (NoSuchElementException ex){
            response.setStatus(404);
            return "Failed to process request";
        } catch (Exception ex) {
            response.setStatus(401);
            return "Failed to process request";
        }
    }


    @PostMapping("/user/login")
    @ResponseBody
    public String login(HttpServletResponse response, @RequestBody UserForm form) throws JsonProcessingException {
        String userId = form.getId();
        String password = form.getPassword();

        try{
            userService.checkIdAndPassword(userId, password);
        } catch(NoSuchElementException ex){
            response.setStatus(404);
            String notExistId = messageSource.getMessage("error.NotExistID", null, Locale.KOREA);
            return notExistId;
        } catch(IllegalAccessException ex){
            response.setStatus(404);
            return ex.getMessage();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userId, form.getPassword())
        );

        Map<String, String> tokens = jwtUtil.generateTokens(userId, 1000 * 60 * 10);  // 10min
        String token = new ObjectMapper().writeValueAsString(tokens);
        userService.updateRefreshToken(userId, tokens.get("refresh-token"));

        return token;
    }

    @PostMapping("/user/logout")
    @ResponseBody
    public String logout(HttpServletRequest httpServletRequest) {
        // refreshToken 제거
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String userId = jwtUtil.extractName(token);

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
