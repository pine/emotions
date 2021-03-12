package moe.pine.emotions.app.services;

import lombok.SneakyThrows;
import moe.pine.emotions.bookmeter.Bookmeter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SuppressWarnings({"ConstantConditions", "NullableProblems"})
public class BookmeterServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Bookmeter bookmeter;

    @InjectMocks
    private BookmeterService bookmeterService;

    @Test
    @SneakyThrows
    public void updateImageTest() {
        final byte[] imageBytes = new byte[]{0x00, 0x01, 0x02};

        doNothing().when(bookmeter).updateProfileImage(imageBytes);

        bookmeterService.updateImage(imageBytes);

        verify(bookmeter).updateProfileImage(imageBytes);
    }

    @Test
    @SneakyThrows
    public void updateImageTest_nullImage() {
        expectedException.expectMessage("`image` should not be empty.");
        expectedException.expect(IllegalArgumentException.class);

        bookmeterService.updateImage(null);

        verify(bookmeter, never()).updateProfileImage(any());
    }
}
