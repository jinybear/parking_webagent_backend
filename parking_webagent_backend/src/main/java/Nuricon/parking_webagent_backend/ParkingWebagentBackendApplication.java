package Nuricon.parking_webagent_backend;

import Nuricon.parking_webagent_backend.util.mqtt.MqttClientAdapter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
public class ParkingWebagentBackendApplication implements ApplicationRunner {

//	@Autowired
//	private MqttGenerator gen;

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		var t = new MqttClientAdapter("tcp://192.168.0.250:1883", "/average/#");
//		var t1 = new MqttClientAdapter("tcp://192.168.0.203:1883", "/average/#");
	}

	public static void main(String[] args){
		SpringApplication.run(ParkingWebagentBackendApplication.class, args);
	}


}
