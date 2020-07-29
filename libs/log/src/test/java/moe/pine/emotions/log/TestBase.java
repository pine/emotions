package moe.pine.emotions.log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static org.mockito.Mockito.spy;

@SuppressWarnings({"WeakerAccess", "JUnitTestCaseWithNoTests", "NotNullFieldNotInitialized"})
public class TestBase {
    private static final int REDIS_DATABASE = 1;

    protected StringRedisTemplate redisTemplate;

    @Rule
    @SuppressWarnings("rawtypes")
    public GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:6.0.6"))
        .withExposedPorts(6379);

    @Before
    public void setUp() {
        final var configuration = new RedisStandaloneConfiguration(redis.getHost(), redis.getFirstMappedPort());
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
