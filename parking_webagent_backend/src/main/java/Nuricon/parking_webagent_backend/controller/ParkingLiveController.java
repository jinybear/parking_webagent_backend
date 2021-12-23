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

    // 라이브 메뉴 선택시
    @PostMapping("/api/live")
    @ResponseBody
    public List<Source> ParkingLiveSourceList(){
        return parkingLiveService.sourceKey();
    }

    //주차장 선택시 : 전체, 구역 만공차
    @PostMapping("/api/parkingLot")
    @ResponseBody
    public List<ParkingLiveVO> parkingLive(String areaId){
        System.out.println(areaId + "주차장아이디");
        return parkingLiveService.parkingLive(areaId);
    }

    //주차장 선택시 : 구역외
    @PostMapping("/api/outParking")
    @ResponseBody
    public List<Integer> outParkingLive(String areaId){
        return parkingLiveService.outParkingLive(areaId);
    }
}