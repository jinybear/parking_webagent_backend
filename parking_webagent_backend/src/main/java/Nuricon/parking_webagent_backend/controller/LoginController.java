package Nuricon.parking_webagent_backend.controller;

import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.service.UserService;
import Nuricon.parking_webagent_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/user")
    public User account(@RequestBody UserForm form){
        User user = new User(form.getId(), form.getPassword());
        this.userService.registUser(user);

        return user;
    }

    @PostMapping("/user/login")
    @ResponseBody
    public String login(HttpServletResponse response, @RequestBody UserForm form) {
        var id = form.getId();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(id, form.getPassword())
        );
        String token = jwtUtil.generateToken(id);

        Cookie myCookie = new Cookie("Authorization", token);
        myCookie.setMaxAge(1000 * 60 * 60 * 10);
        response.addCookie(myCookie);

        return token;
    }

    @PostMapping("/user/logout")
    @ResponseBody
    public String logout(HttpServletResponse response) {
        Cookie myCookie = new Cookie("Authorization", null);
        myCookie.setMaxAge(0);
        myCookie.setPath("/");

        response.addCookie(myCookie);

        return "OK";
    }
}
