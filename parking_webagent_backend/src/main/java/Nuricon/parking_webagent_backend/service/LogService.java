package Nuricon.parking_webagent_backend.service;

import Nuricon.parking_webagent_backend.domain.Log;
import Nuricon.parking_webagent_backend.repository.LogRepository;
import Nuricon.parking_webagent_backend.util.enums.LogLevel;
import Nuricon.parking_webagent_backend.util.enums.Role;
import Nuricon.parking_webagent_backend.util.mqtt.MqttClientAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

@Transactional
@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    private Logger logger = LoggerFactory.getLogger(LogService.class);

    public void write(String message, LogLevel level, String srcIPAddress) {
        try {
            Log log = new Log();
            log.setDescription(message);
            log.setLevel(level.toString());
            log.setSrcIpaddress(srcIPAddress);
            log.setCreatedAt(LocalDateTime.now());

            logRepository.save(log);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public Page<Log> Read(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);


        return logRepository.findAll(pageRequest);
    }

}
