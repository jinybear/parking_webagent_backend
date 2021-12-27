package Nuricon.parking_webagent_backend.util.SharedMemory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class SharedMemory {
    //region <Edge 상태 모니터링 관련 class, Generator, memoryMap>
    @Data
    @AllArgsConstructor
    public class EdgeStatus {
        private LocalDateTime engineTimeStamp;
    }

    public EdgeStatus GenerateEdgeStatus(LocalDateTime engineTimeStamp){
        return new EdgeStatus(engineTimeStamp);
    }

    public Map<String, LocalDateTime> edgeStatusMap = new HashMap<>();
    //endregion


    // dataManager 에게 "Infersources" 요청에 대한 응답을 담는 변수
    public String dataManagerInfersources;
}


