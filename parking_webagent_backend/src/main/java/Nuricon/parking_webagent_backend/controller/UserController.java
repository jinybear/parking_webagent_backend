package Nuricon.parking_webagent_backend.controller;

import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.service.UserService;
import Nuricon.parking_webagent_backend.util.JwtUtil;
import Nuricon.parking_webagent_backend.util.enums.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

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
            response.setStatus(403);
            return ex.getMessage();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userId, form.getPassword())
        );

//        Cookie myCookie = new Cookie("Authorization", token);
//        myCookie.setMaxAge(1000 * 60 * 60 * 10);
//        response.addCookie(myCookie);

//        Map<String, String> tokens = jwtUtil.generateToken(userId);
//        String json = new ObjectMapper().writeValueAsString(tokens);
//
//        response.addHeader("access-token", tokens.get("access-token"));
//        response.addHeader("refresh-token", tokens.get("refresh-token"));

//        return tokens.get("access-token");

        String token = jwtUtil.generateToken(userId);
        return token;
    }

    @PostMapping("/user/logout")
    @ResponseBody
    public String logout(HttpServletResponse response) {
//        Cookie myCookie = new Cookie("Authorization", null);
//        myCookie.setMaxAge(0);
//        myCookie.setPath("/");

        //response.addCookie(myCookie);

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