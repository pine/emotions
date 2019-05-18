package moe.pine.emotions;

import moe.pine.heroku.addons.HerokuRedis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {
    public static void main(String[] args) {
        if (HerokuRedis.isDetected()) {
            System.setProperty("spring.redis.host", StringUtils.defaultString(HerokuRedis.getHost()));
            System.setProperty("spring.redis.password", StringUtils.defaultString(HerokuRedis.getPassword()));
            System.setProperty("spring.redis.port", Integer.toString(HerokuRedis.getPort()));
        }

        SpringApplication.run(App.class, args);
    }
}
