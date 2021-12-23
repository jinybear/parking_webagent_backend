package Nuricon.parking_webagent_backend;

import Nuricon.parking_webagent_backend.util.mqtt.MqttClientAdapter;
import Nuricon.parking_webagent_backend.util.mqtt.strategy.CheckEdgeStateStrategy;
import Nuricon.parking_webagent_backend.util.mqtt.strategy.Strategy;
import Nuricon.parking_webagent_backend.util.ymlreader.ConstructorYml;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.Locale;

//@SpringBootApplication
//public class ParkingWebagentBackendApplication extends SpringBootServletInitializer {
//	public static void main(String[] args) {
//		SpringApplication.run(ParkingWebagentBackendApplication.class, args);
//	}
//
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return super.configure(builder);
//	}
//}

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(value = {ConstructorYml.class})
public class ParkingWebagentBackendApplication implements ApplicationRunner {
	@Autowired
	private ConstructorYml constructorYml;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (constructorYml.getMqtt().isEnable()) {
			constructorYml.getMqtt().getUrls().forEach((url) -> {
				try {
					String[] topics = {"/decision/#", "/average/#"};
					var t = new MqttClientAdapter(url, topics, new CheckEdgeStateStrategy());
				} catch (MqttException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public static void main(String[] args){
		SpringApplication.run(ParkingWebagentBackendApplication.class, args);
	}
}
