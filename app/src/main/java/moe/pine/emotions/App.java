package moe.pine.emotions;

import moe.pine.heroku.addons.HerokuRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {
    public static void main(String[] args) {
        final HerokuRedis redis = HerokuRedis.get();
        if (redis != null) {
            System.setProperty("spring.redis.host", redis.getHost());
            System.setProperty("spring.redis.password", redis.getPassword());
            System.setProperty("spring.redis.port", Integer.toString(redis.getPort()));
        }

        SpringApplication.run(App.class, args);
    }
}
