package Nuricon.parking_webagent_backend.util.SharedMemory;
import Nuricon.parking_webagent_backend.util.beanCaller.BeanUtils;
import Nuricon.parking_webagent_backend.util.mqtt.strategy.CheckEdgeStateStrategy;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.hibernate.sql.Update;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import Nuricon.parking_webagent_backend.util.ymlreader.ConstructorYml;

import java.time.LocalDateTime;


/**
 * SharedMemory 에서 관리하는 데이터를 주기적으로 갱신하기 위한 Bean 객체
 * @scheduled 를 이용해 주기적으로 데이터 갱신을 위한 로직을 실행함.
 */
@Component
public class UpdateSharedMemory {
    private SharedMemory sharedMemory;
    private ConstructorYml constructorYml;

    private Logger logger = LoggerFactory.getLogger(UpdateSharedMemory.class);
    private WebClient client = null;

    public UpdateSharedMemory(SharedMemory sharedMemory, ConstructorYml constructorYml) {
        this.sharedMemory = sharedMemory;
        this.constructorYml = constructorYml;

        if(constructorYml.getDataManager().isEnable())
            this.client = createRestClient();
    }

    // 30초 간격으로 DataManager 에 REST 요청 및 결과를 SharedMemory 에 저장하는 스케줄링 task
    @Scheduled(fixedDelay = 30000)
    private void requestSourceInfo(){
        if (this.client == null)
            return;

        String url = constructorYml.getDataManager().getUrl();
        String uri = constructorYml.getDataManager().getUri();

        try {
            logger.debug(String.format("Try to requestSourceInfo [%s/%s]", url, uri));
            // 수집한 sourceIDs 는 어떻게?
            String res = client.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue("{}"))
                    .retrieve()
                    .bodyToMono(String.class).block();

            logger.debug("ResponseSourceInfo data : " + res);

            synchronized (this){
                sharedMemory.dataManagerInfersources = res;
            }
            logger.debug("Update dataManagerInfersource : " + sharedMemory.dataManagerInfersources);

        } catch(Exception e) {
            logger.error("Failed to request");
            logger.warn(e.getMessage());
        }
    }

    // datamanager url 과 통신하는 client 생성
    WebClient createRestClient() {
        HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5))
                        .addHandlerLast(new WriteTimeoutHandler(5)));

        String url = constructorYml.getDataManager().getUrl();
        WebClient client = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE)
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();

        return client;
    }
}
