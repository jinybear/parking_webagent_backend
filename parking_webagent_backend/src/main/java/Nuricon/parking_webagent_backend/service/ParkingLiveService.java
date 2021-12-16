package Nuricon.parking_webagent_backend.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Nuricon.parking_webagent_backend.VO.ParkingLiveVO;
import Nuricon.parking_webagent_backend.domain.OutStatistics;
import Nuricon.parking_webagent_backend.domain.Source;
import Nuricon.parking_webagent_backend.domain.Statistics;
import Nuricon.parking_webagent_backend.repository.OutStatisticsRepository;
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

    //메인메뉴 리스트 -> 라이브 소분류
    public List<Source> sourceKey(){
        List<Integer> _areaList = new ArrayList<>(Arrays.asList(1001,1002,2001,31));
        List<Source> _sourceList = sourceRepository.findByAreaIdIn(_areaList);
        System.out.println(_sourceList);
        return _sourceList;
    }

    //주차장별 정보
    public List<ParkingLiveVO> parkingLive(String areaId){
        int _areaId = Integer.valueOf(areaId);
        //현재 시간 기준 날짜,시간,분이 크거나 같은 데이터 출력
        LocalDateTime _date = LocalDateTime.now().withSecond(0).withNano(0);
        List<Statistics> _statistic = statisticsRepository.findByAreaIdAndDateGreaterThanEqual(_areaId, _date);
        //주차장 구역 리스트 생성
        List<Integer> _totalSectorId = _statistic.stream().map(x->x.getSectorId()).distinct().collect(Collectors.toList());
        List<ParkingLiveVO> _parkingVo = new ArrayList<ParkingLiveVO>();
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

    public List<Integer> outParkingLive(String areaId){
        int _areaId = Integer.valueOf(areaId);
        LocalDateTime _date = LocalDateTime.now().withSecond(0).withNano(0);
        List<OutStatistics> outStatistics = outStatisticsRepository.findByAreaIdAndDateGreaterThanEqual(_areaId, _date);
        System.out.println(outStatistics + "어디로 가야 하오");
        Integer _illegalParkingAreaCount = (int)(long)outStatistics.stream().map(x -> x.getSourceId()).distinct().count(); //불법 주차 구역 개수
        System.out.println(_illegalParkingAreaCount + "깽꺵쓰");
        List<OutStatistics> _illegalParkingStayList = outStatistics.stream().filter(x -> x.getParkingCount() != 0).collect(Collectors.toList());
        System.out.println(_illegalParkingStayList + "영웅이라뇨 저는 그저 망치를 든 요들일뿌니에여");

        Integer _illegalParkingStayCount = _illegalParkingStayList.stream().map(x -> x.getParkingCount()).reduce(0, (prev, cur) -> prev + cur);
        System.out.println(_illegalParkingStayCount + "하위핳위");
        List<Integer> illegalParking = new ArrayList<>();
        illegalParking.add(_illegalParkingAreaCount);
        illegalParking.add(_illegalParkingStayCount);
        return illegalParking;
    }
}
