package moe.pine.emotions.app.services;

import moe.pine.emotions.bookmeter.Bookmeter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("ConstantConditions")
public class BookmeterServiceTest {
    @Mock
    private Bookmeter bookmeter;

    @InjectMocks
    private BookmeterService bookmeterService;

    @Test
    public void updateImageTest() throws Exception {
        final byte[] imageBytes = {0x00, 0x01, 0x02};

        doNothing().when(bookmeter).updateProfileImage(imageBytes);

        bookmeterService.updateImage(imageBytes);

        verify(bookmeter).updateProfileImage(imageBytes);
    }

    @Test
    public void updateImageTest_nullImage() throws Exception {
        assertThatThrownBy(() -> bookmeterService.updateImage(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("`image` should not be empty.");

        verify(bookmeter, never()).updateProfileImage(any());
    }
}
