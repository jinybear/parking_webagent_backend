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
        User user = new User(form.getName(), form.getPassword());
        this.userService.registUser(user);

        return user;
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody UserForm form) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.getName(), form.getPassword())
        );

        return jwtUtil.generateToken(form.getName());
    }
}
