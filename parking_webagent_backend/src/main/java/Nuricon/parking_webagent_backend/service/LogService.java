package Nuricon.parking_webagent_backend.service;

import Nuricon.parking_webagent_backend.domain.Log;
import Nuricon.parking_webagent_backend.repository.LogRepository;
import Nuricon.parking_webagent_backend.util.enums.LogLevel;
import Nuricon.parking_webagent_backend.util.enums.Role;
import Nuricon.parking_webagent_backend.util.mqtt.MqttClientAdapter;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * log table 에서 지정한 page, size, asc(정렬) 옵션기준 데이터 추출 함수
     * @param page
     * @param size
     * @param asc
     * @return
     */
    public Page<Log> Read(int page, int size, boolean asc){
        Sort sort = asc ? Sort.by("createdAt") : Sort.by("createdAt").descending();
        Pageable pageRequest = PageRequest.of(page, size, sort);

        return logRepository.findAll(pageRequest);
    }
}
