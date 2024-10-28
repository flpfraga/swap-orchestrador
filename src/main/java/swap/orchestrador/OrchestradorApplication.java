package swap.orchestrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrchestradorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrchestradorApplication.class, args);
	}

}
