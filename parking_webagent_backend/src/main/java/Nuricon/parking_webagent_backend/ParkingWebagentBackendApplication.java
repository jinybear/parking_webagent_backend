package Nuricon.parking_webagent_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

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
public class ParkingWebagentBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(ParkingWebagentBackendApplication.class, args);
	}
}
