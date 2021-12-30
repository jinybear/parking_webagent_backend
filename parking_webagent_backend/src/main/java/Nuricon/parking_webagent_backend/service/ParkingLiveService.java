package Nuricon.parking_webagent_backend.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import Nuricon.parking_webagent_backend.VO.ParkingLiveVO;
import Nuricon.parking_webagent_backend.domain.*;
import Nuricon.parking_webagent_backend.repository.OutStatisticsRepository;
import Nuricon.parking_webagent_backend.repository.OutsidesRepository;
import Nuricon.parking_webagent_backend.repository.SourceRepository;
import Nuricon.parking_webagent_backend.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingLiveService {
    @Autowired
    private SourceRepository sourceRepository;
    @Autowired
    private StatisticsRepository statisticsRepository;
    @Autowired
    private OutStatisticsRepository outStatisticsRepository;
    @Autowired
    private OutsidesRepository outsidesRepository;

    //메인메뉴 리스트 -> 라이브 소분류
    public List<Source> sourceKey(){
        //List<Integer> _areaList = new ArrayList<>(Arrays.asList(1002,2001,2011,2021,2031));
        List<Integer> _areaList = new ArrayList<>(Arrays.asList(2001));
        List<Source> _sourceList = sourceRepository.findByAreaIdIn(_areaList);
        //System.out.println(_sourceList);
        return _sourceList;
    }

    //주차장별 정보
    public List<ParkingLiveVO> parkingLive(String areaId){
        int _areaId = Integer.valueOf(areaId);
        //현재 시간 기준 날짜,시간,분이 크거나 같은 데이터 출력
        LocalDateTime _date = LocalDateTime.now().withSecond(0).withNano(0);
//        LocalDateTime _date = LocalDateTime.of(2021,11,10,00,00,00);
        List<Statistics> _statistic = statisticsRepository.findByAreaIdAndDateGreaterThanEqual(_areaId, _date);

        //주차장 구역 리스트 생성
        List<Integer> _totalSectorId = _statistic.stream().map(x->x.getSectorId()).distinct().collect(Collectors.toList());

        List<ParkingLiveVO> _parkingVo = new ArrayList<>();
        //구역별 정보 리스트 생성
        for(int sectId : _totalSectorId){
            ParkingLiveVO _parkingLiveVO = new ParkingLiveVO();
            long parkingFull = _statistic.stream().filter(t -> t.getSectorId() == sectId && t.getParkingStatus() == 1).count();
            long parkingTotal = _statistic.stream().filter(t -> t.getSectorId() == sectId).count();
            long parkingEmpty = parkingTotal - parkingFull;
            double _fullPercent = Math.round(parkingFull/(parkingTotal * 1.0) * 100);
            String fullPercent = String.format("%.2f", _fullPercent);

            _parkingLiveVO.setId(sectId);
            _parkingLiveVO.setParkingFull(parkingFull);
            _parkingLiveVO.setParkingEmpty(parkingEmpty);
            _parkingLiveVO.setParkingTotal(parkingTotal);
            _parkingLiveVO.setFullPercent(fullPercent);
            _parkingVo.add(_parkingLiveVO);
        }
        return _parkingVo;
    }

    //주차장별 구역외
    public List<Integer> outParkingLive(String areaId){
        int _areaId = Integer.valueOf(areaId);
        LocalDateTime _date = LocalDateTime.now().withSecond(0).withNano(0);
//        LocalDateTime _date = LocalDateTime.of(2021,12,19,00,00,00);
        List<OutStatistics> outStatistics = outStatisticsRepository.findByAreaIdAndDateGreaterThanEqual(_areaId, _date); //불법 주차 차량 대수 구하기 위한 list
        Integer _illegalParkingStayCount  = outStatistics.stream().filter(x -> x.getSourceId() == 0).map((m)->m.getParkingCount()).findFirst().get(); //불법 주차 차량 대수
        //불법 주차 구역 개수
        List<Outsides> _illegalParkingAreaList = outsidesRepository.findAll(); //모든 불법 주차 카메라 리스트
        List<Outsides> _illegalParkingAreaIDList = _illegalParkingAreaList.stream().filter(x -> x.getSourceId().startsWith(areaId)).collect(Collectors.toList()); //해당 주차장 불법 주차 카메라 리스트
        List<Integer> _illegalParkingAreaCount = new ArrayList<>();
        for(Outsides outsides : _illegalParkingAreaIDList){
            for(Sidezones sidezones : outsides.getSidezones()){
                _illegalParkingAreaCount.add(sidezones.getSidezoneId());
            }
        }

        List<Integer> illegalParking = new ArrayList<>();
        illegalParking.add(_illegalParkingAreaCount.size());
        illegalParking.add(_illegalParkingStayCount);
        return illegalParking;
    }

    //카메라별 감시 대상 불법 주차 구역 개수
    public int cameraOutParking(String sourceId){
        int _sourceId = Integer.valueOf(sourceId);
        List<Outsides> _sideZone = outsidesRepository.findBySourceId(_sourceId);
        List<Integer> outParkingList = new ArrayList<Integer>();
        for (Outsides outsides : _sideZone){
            for(Sidezones sidezones : outsides.getSidezones()){
                Integer _cameraOutParkingCount = Integer.valueOf(sidezones.getSidezoneId());
                outParkingList.add(_cameraOutParkingCount);
            }
        }
        int outParkingCount = outParkingList.size();
        return outParkingCount;
    }
}
