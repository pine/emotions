package moe.pine.emotions;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {
    public static void main(String[] args) {
        final String port = System.getenv("PORT");
        if (StringUtils.isNotEmpty(port)) {
            System.setProperty("server.port", port);
        }

        SpringApplication.run(App.class, args);
    }
}
