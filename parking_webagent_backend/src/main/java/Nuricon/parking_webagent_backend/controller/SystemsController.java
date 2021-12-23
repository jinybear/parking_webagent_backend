package Nuricon.parking_webagent_backend.controller;

import Nuricon.parking_webagent_backend.domain.Edge;
import Nuricon.parking_webagent_backend.domain.EdgeSource;
import Nuricon.parking_webagent_backend.domain.Log;
import Nuricon.parking_webagent_backend.repository.EdgeRepository;
import Nuricon.parking_webagent_backend.repository.EdgeSourceRepository;
import Nuricon.parking_webagent_backend.service.LogService;
import Nuricon.parking_webagent_backend.service.UserService;
import Nuricon.parking_webagent_backend.util.SharedMemory.SharedMemory;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class SystemsController {
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;
    @Autowired
    private SharedMemory sharedMemory;
    @Autowired
    private EdgeRepository edgeRepository;
    @Autowired
    private EdgeSourceRepository edgeSourceRepository;
    @Autowired
    private MessageSource messageSource;

    @PostMapping("/api/systems/unlock_account")
    public String unlock_account(HttpServletResponse response, @RequestParam String userId) {
        try {
            userService.unlock(userId);
        } catch (NoSuchElementException ex){
            String msg = messageSource.getMessage("error.NotExistID", null, Locale.getDefault());
            response.setStatus(403);
            return msg;
        }

        return userId;
    }

    @GetMapping("/api/systems/log")
    public Page<Log> logs(@RequestParam int page, @RequestParam int size, @RequestParam boolean asc) {
        return logService.Read(page, size, asc);
    }

    @GetMapping("/api/systems/edgestatus")
    public List<EdgeStatus> edgeStatus(){
        List<EdgeStatus> res = new ArrayList<>();

        // sharedMemory에 매칭되는 sourceId는 Edge list에 반영
        for(Integer key : sharedMemory.edgeStatusMap.keySet()){
            List<EdgeSource> sources = edgeSourceRepository.findBySourceId(key);
            if(!sources.isEmpty()) {
                EdgeSource source = sources.get(0);
                //if (res.stream().filter((x) -> x.getName().equals(source.getEdge().getName())).count() > 0) {
                if(res.stream().anyMatch((x) -> x.getName().equals(source.getEdge().getName()))){
                    res.removeAll(res.stream().filter((x) -> x.getName().equals(source.getEdge().getName()))
                            .collect(Collectors.toList()));
                }

                EdgeStatus status = new EdgeStatus(source.getEdge().getName(), sharedMemory.edgeStatusMap.get(key));
                res.add(status);
            }
        }
        return res;
    }

    @Data
    @AllArgsConstructor
    public class EdgeStatus{
        private String name;
        private LocalDateTime timestamp;
    }
}
