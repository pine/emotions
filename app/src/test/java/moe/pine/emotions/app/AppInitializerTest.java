package moe.pine.emotions.app;

import moe.pine.heroku.addons.HerokuRedis;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class AppInitializerTest {
    @Test
    public void runTest() throws Exception {
        HerokuRedis redis = mock(HerokuRedis.class);
        when(redis.getHost()).thenReturn("host");
        when(redis.getPassword()).thenReturn("password");
        when(redis.getPort()).thenReturn(12345);

        try (var mocked = mockStatic(HerokuRedis.class)) {
            when(HerokuRedis.get()).thenReturn(redis);

            System.clearProperty("spring.redis.host");
            System.clearProperty("spring.redis.password");
            System.clearProperty("spring.redis.port");

            AppInitializer.run();
        }

        assertEquals("host", System.getProperty("spring.redis.host"));
        assertEquals("password", System.getProperty("spring.redis.password"));
        assertEquals("12345", System.getProperty("spring.redis.port"));
    }
}
