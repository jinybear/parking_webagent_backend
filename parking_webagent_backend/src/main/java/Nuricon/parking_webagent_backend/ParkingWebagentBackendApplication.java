package Nuricon.parking_webagent_backend;

import Nuricon.parking_webagent_backend.util.mqtt.MqttClientAdapter;
import Nuricon.parking_webagent_backend.util.ymlreader.ConstructorYml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

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

@SpringBootApplication
@EnableConfigurationProperties(value = {ConstructorYml.class})
public class ParkingWebagentBackendApplication implements ApplicationRunner {
	@Autowired
	private ConstructorYml constructorYml;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//var t = new MqttClientAdapter("tcp://192.168.0.250:1883", "/average/#");

//		constructorYml.getMqtt().getUrls().forEach((url) -> {
//			System.out.println("url : " + url);
//		});
	}

	public static void main(String[] args){
		SpringApplication.run(ParkingWebagentBackendApplication.class, args);
	}
}
