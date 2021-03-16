package moe.pine.emotions.app.services;

import moe.pine.emotions.app.properties.GravatarProperties;
import moe.pine.emotions.gravatar.Gravatar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GravatarServiceTest {
    @Mock
    private Gravatar gravatar;

    @Mock
    private GravatarProperties gravatarProperties;

    @InjectMocks
    private GravatarService gravatarService;

    @Test
    public void chooseImageTest() {
        @SuppressWarnings("unchecked") final List<String> images = Mockito.mock(List.class);
        @SuppressWarnings("unchecked") final List<String> addresses = Mockito.mock(List.class);

        when(gravatarProperties.getImages()).thenReturn(images);
        when(gravatarProperties.getAddresses()).thenReturn(addresses);
        doNothing().when(gravatar).chooseImage(images, addresses);

        gravatarService.chooseImage();

        verify(gravatarProperties).getImages();
        verify(gravatarProperties).getAddresses();
        verify(gravatar).chooseImage(images, addresses);
    }
}
