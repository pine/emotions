package moe.pine.emotions.log.utils;

import moe.pine.emotions.log.AvatarUpdatedKeyBuilder;
import moe.pine.emotions.log.AvatarType;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

public class AvatarUpdatedKeyBuilderTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private AvatarUpdatedKeyBuilder keyBuilder;

    @Test
    public void buildKeyTest() {
        assertEquals(
            "avatar_updated:gravatar",
            keyBuilder.buildKey(AvatarType.GRAVATAR));
    }
}
