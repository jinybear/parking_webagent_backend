package Nuricon.parking_webagent_backend.controller;

import Nuricon.parking_webagent_backend.VO.ParkingLiveVO;
import Nuricon.parking_webagent_backend.domain.Source;
import Nuricon.parking_webagent_backend.service.ParkingLiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParkingLiveController {
    @Autowired
    ParkingLiveService parkingLiveService;

    @PostMapping("/live")
    @ResponseBody
    public List<Source> ParkingLiveSourceList(){
        return parkingLiveService.sourceKey();
    }

    @PostMapping("/parkingLot")
    @ResponseBody
    public List<ParkingLiveVO> parkingLive(String areaId){
        System.out.println(areaId + "주차장아이디");
        return parkingLiveService.parkingLive(areaId);
    }

    @PostMapping("/OutParking")
    @ResponseBody
    public List<Integer> outParkingLive(String areaId){
        return parkingLiveService.outParkingLive(areaId);
    }
}