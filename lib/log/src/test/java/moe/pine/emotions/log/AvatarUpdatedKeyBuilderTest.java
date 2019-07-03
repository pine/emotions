package moe.pine.emotions.log;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("NullableProblems")
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
