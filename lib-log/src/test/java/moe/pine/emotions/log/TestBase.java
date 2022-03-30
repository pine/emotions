package moe.pine.emotions.log;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.lang.Nullable;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static org.mockito.Mockito.spy;

@SuppressWarnings({
    "JUnitTestCaseWithNoTests",
    "NotNullFieldNotInitialized",
    "WeakerAccess",
    "rawtypes",
})
public class TestBase {
    private static final String REDIS_IMAGE = "redis:6.0.6";
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final int REDIS_DATABASE = 1;

    protected ReactiveStringRedisTemplate redisTemplate;

    @Nullable
    private GenericContainer container;

    @BeforeEach
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

        final var redisTemplate = new ReactiveStringRedisTemplate(factory);
        this.redisTemplate = spy(redisTemplate);
    }

    @AfterEach
    public void tearDown() {
        redisTemplate.execute(conn -> conn.serverCommands().flushDb())
            .blockLast();

        if (container != null) {
            container.close();
            container = null;
        }
    }
}
