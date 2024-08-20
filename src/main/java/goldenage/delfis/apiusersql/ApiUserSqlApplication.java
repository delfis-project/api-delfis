package goldenage.delfis.apiusersql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ApiUserSqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiUserSqlApplication.class, args);
    }

}
