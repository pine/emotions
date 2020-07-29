package moe.pine.emotions.log;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static org.mockito.Mockito.spy;

@SuppressWarnings({"WeakerAccess", "JUnitTestCaseWithNoTests", "NotNullFieldNotInitialized", "rawtypes"})
public class TestBase {
    private static final String REDIS_IMAGE = "redis:6.0.6";
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final int REDIS_DATABASE = 1;

    protected StringRedisTemplate redisTemplate;

    @Nullable
    private GenericContainer container;

    @Before
    public void setUp() {
        final RedisStandaloneConfiguration configuration;
        if (StringUtils.isNotEmpty(System.getenv("CI"))) {
            configuration = new RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT);
        } else {
            container = new GenericContainer(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(REDIS_PORT);
            container.start();

            configuration = new RedisStandaloneConfiguration(container.getHost(), container.getFirstMappedPort());
        }
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

        if (container != null) {
            container.close();
            container = null;
        }
    }
}
