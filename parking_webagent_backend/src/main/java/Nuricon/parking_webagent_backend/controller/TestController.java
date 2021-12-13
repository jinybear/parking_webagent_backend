package Nuricon.parking_webagent_backend.controller;

import Nuricon.parking_webagent_backend.domain.HourSummary;
import Nuricon.parking_webagent_backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

//    @Autowired
//    private EventService es;
//
//
//
//    @Autowired
//    private ParkingCustomRepository parkingCustomRepository;
//
//    @GetMapping("/test_getMongoData")
//    public HourSummary getTest()
//    {
//        var user = es.getTest("6172e011994ad631bc359077");
//        return user.get();
//    }
//
//
//    @PostMapping("/test_sendToMqtt")
//    public void sendToMqtt(String message, String topic)
//    {
//        // 요청 수신시 대상 mqtt broker에게 메시지 전송 테스트
//        //mqttOrderGateway.sendToMqtt(message, topic);
//    }
//
//    @GetMapping("/test_getparking")
//    @ResponseBody
//    public List<ParkingArea> test_getparking()
//    {
//        return parkingCustomRepository.findAllParkingInfo();
//    }
}
