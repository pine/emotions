package moe.pine.emotions.log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvatarUpdatedRepositoryTest extends TestBase {
    private static final String GRAVATAR_KEY = "avatar_updated:gravatar";
    private static final String SLACK_KEY = "avatar_updated:slack";
    private static final String TWITTER_KEY = "avatar_updated:twitter";
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Tokyo");

    @Mock
    private AvatarUpdatedKeyBuilder keyBuilder;

    private AvatarUpdatedRepository avatarUpdatedRepository;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        avatarUpdatedRepository = new AvatarUpdatedRepository(
            redisTemplate,
            keyBuilder,
            ZONE_ID
        );
    }

    @Test
    public void setTest() {
        final LocalDateTime now = LocalDateTime.of(2019, 5, 13, 17, 46, 12);

        when(keyBuilder.buildKey(AvatarType.GRAVATAR)).thenReturn(GRAVATAR_KEY);

        avatarUpdatedRepository.set(AvatarType.GRAVATAR, now);

        verify(keyBuilder).buildKey(AvatarType.GRAVATAR);
        verify(redisTemplate).opsForValue();

        final String value = redisTemplate.opsForValue().get(GRAVATAR_KEY).block();
        assertNotNull(value);

        final long epochSeconds = Long.parseLong(value);
        assertEquals(1557737172L, epochSeconds);
    }

    @Test
    public void mgetTest() {
        final List<AvatarType> avatarTypes =
            List.of(
                AvatarType.GRAVATAR,
                AvatarType.SLACK,
                AvatarType.TWITTER);

        when(keyBuilder.buildKey(AvatarType.GRAVATAR)).thenReturn(GRAVATAR_KEY);
        when(keyBuilder.buildKey(AvatarType.SLACK)).thenReturn(SLACK_KEY);
        when(keyBuilder.buildKey(AvatarType.TWITTER)).thenReturn(TWITTER_KEY);

        redisTemplate.opsForValue().set(GRAVATAR_KEY, String.valueOf(1557737172L)).block();
        redisTemplate.opsForValue().set(SLACK_KEY, String.valueOf(1557738119L)).block();
        redisTemplate.opsForValue().set(TWITTER_KEY, String.valueOf(1557670564L)).block();

        final var items = avatarUpdatedRepository.mget(avatarTypes);
        assertEquals(3, items.size());

        assertEquals(AvatarType.GRAVATAR, items.get(0).getKey());
        assertEquals(
            LocalDateTime.of(2019, 5, 13, 17, 46, 12),
            items.get(0).getValue());

        assertEquals(AvatarType.SLACK, items.get(1).getKey());
        assertEquals(
            LocalDateTime.of(2019, 5, 13, 18, 1, 59),
            items.get(1).getValue());

        assertEquals(AvatarType.TWITTER, items.get(2).getKey());
        assertEquals(
            LocalDateTime.of(2019, 5, 12, 23, 16, 4),
            items.get(2).getValue());
    }
}
