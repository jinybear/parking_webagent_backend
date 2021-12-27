package Nuricon.parking_webagent_backend.util.mqtt.strategy;
import Nuricon.parking_webagent_backend.util.SharedMemory.SharedMemory;
import Nuricon.parking_webagent_backend.util.beanCaller.BeanUtils;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// /decision/# 에 대한 메시지 수신
public class CheckEdgeStateStrategy implements Strategy {
    SharedMemory sharedMemory = (SharedMemory) BeanUtils.getBean(SharedMemory.class);
    Logger logger = LoggerFactory.getLogger(CheckEdgeStateStrategy.class);

    @Override
    public void processMessage(String topic, MqttMessage message) {
        if (topic.contains("/decision/")) {
            try{
                // 수신한 메시지를 캐리지리턴 기준으로 쪼갠(sourceId 구분) 후, 쪼갠 메시지에서 sourceId 추출
                logger.debug("Receive mqtt public : " + message.toString());
                String[] test = message.toString().split("\n");

                String sourceID = message.toString().split("/")[0];

                // 수집한 sourceIDs 는 어떻게?
                if (sharedMemory.dataManagerInfersources != null) {
                    if (sharedMemory.edgeStatusMap.containsKey(sourceID)) {
                        LocalDateTime prevTime = sharedMemory.edgeStatusMap.get(sourceID);
                        if (prevTime != null && Duration.between(prevTime, LocalDateTime.now()).getSeconds() <= 5) {
                            return;
                        }
                    }

                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(sharedMemory.dataManagerInfersources);
                    JSONArray arr = (JSONArray) (((JSONObject) json.get("location_data")).get("source_list"));
                    Iterator<JSONObject> iterator = arr.iterator();

                    List<String> sourceIDsInDataManager = new ArrayList<>();
                    while (iterator.hasNext()) {
                        JSONObject subjson = iterator.next();
                        sourceIDsInDataManager.add(subjson.get("source_id").toString());
                    }

                    // 매칭되는 source가 있다면
                    LocalDateTime now = null;
                    if (sourceIDsInDataManager.contains(sourceID)) {
                        // 어디에 저장할래? bean 객체를 만들고 그 아이 내부에서 list로 관리?
                        now = LocalDateTime.now();
                        synchronized (this) {
                            sharedMemory.edgeStatusMap.put(sourceID, now);
                        }
                        logger.debug("Update edgeStatusMap : " + message.toString());
                    }
                }

            } catch(Exception ex){
                logger.error(ex.getMessage());
            }
        } else if(topic.contains("/average/")) {
            try {

            } catch(Exception ex){

            }
        }
    }
}
