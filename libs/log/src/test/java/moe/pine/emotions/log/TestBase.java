package moe.pine.emotions.log;

import org.junit.After;
import org.junit.Before;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.mockito.Mockito.spy;

@SuppressWarnings({"NullableProblems", "WeakerAccess"})
public class TestBase {
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final int REDIS_DATABASE = 1;

    protected StringRedisTemplate redisTemplate;

    @Before
    public void setUp() {
        final var configuration = new RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT);
        configuration.setDatabase(REDIS_DATABASE);

        final var factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet();

        final var redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();

        this.redisTemplate = spy(redisTemplate);
    }

    @After
    public void tearDown() {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.flushDb();
            return null;
        });
    }
}
