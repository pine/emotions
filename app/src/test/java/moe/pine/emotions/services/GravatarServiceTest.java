package moe.pine.emotions.services;

import moe.pine.emotions.gravatar.Gravatar;
import moe.pine.emotions.properties.GravatarProperties;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.mockito.Mockito.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class GravatarServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

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
