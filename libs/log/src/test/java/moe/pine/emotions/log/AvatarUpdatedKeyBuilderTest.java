package moe.pine.emotions.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class AvatarUpdatedKeyBuilderTest {
    @InjectMocks
    private AvatarUpdatedKeyBuilder keyBuilder;

    @Test
    public void buildKeyTest() {
        assertEquals(
            "avatar_updated:gravatar",
            keyBuilder.buildKey(AvatarType.GRAVATAR));
    }
}
