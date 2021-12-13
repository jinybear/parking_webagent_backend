package Nuricon.parking_webagent_backend.service;

import java.util.*;

import Nuricon.parking_webagent_backend.domain.Source;
import Nuricon.parking_webagent_backend.domain.Statistics;
import Nuricon.parking_webagent_backend.repository.SourceRepository;
import Nuricon.parking_webagent_backend.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ParkingLiveService {
    @Autowired
    private SourceRepository sourceRepository;
    @Autowired
    private StatisticsRepository statisticsRepository;

    public List<Source> sourceKey(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1001,1002,2001,31));
        List<Source> sourceList = sourceRepository.findByAreaIdIn(list);
        System.out.println(sourceList);
        //List<Source> sourceList = sourceRepository.findAll();
        return sourceList;
    }

    public List<Statistics> getbyId(String areaId) {
        int _areaId = Integer.valueOf(areaId);
        System.out.println(statisticsRepository.findTopOneByAreaIdOrderByDateDesc(_areaId));
        List<Statistics> _statistic = statisticsRepository.findTop300ByAreaIdOrderByDateDesc(_areaId);
        System.out.println(_areaId + "ㅋㅎㅇㅎㅇ");
        System.out.println((_statistic + "데이터"));
        return _statistic;
    }
}
