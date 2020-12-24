package pablo.motivo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class MotivoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MotivoApplication.class, args);
    }

}
