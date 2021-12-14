package Nuricon.parking_webagent_backend.controller;

import Nuricon.parking_webagent_backend.domain.Log;
import Nuricon.parking_webagent_backend.service.LogService;
import Nuricon.parking_webagent_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@RestController
public class SystemsController {
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;

    @PostMapping("/systems/unlock_account")
    public String unlock_account(HttpServletResponse response, @RequestParam String userId) {
        try {
            userService.unlock(userId);
        } catch (NoSuchElementException ex){
            response.setStatus(403);
            return "요청한 ID가 존재하지 않습니다.";
        }

        return userId;
    }

    @GetMapping("/systems/log")
    public Page<Log> logs(@RequestParam int page, @RequestParam int size) {
        return logService.Read(page, size);
    }
}
