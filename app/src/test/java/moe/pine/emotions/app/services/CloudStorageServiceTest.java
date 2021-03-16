package moe.pine.emotions.app.services;

import moe.pine.emotions.app.properties.CloudStorageProperties;
import moe.pine.emotions.cloudstorage.CloudStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("SpellCheckingInspection")
public class CloudStorageServiceTest {
    @Mock
    private Random random;

    @Mock
    private CloudStorage cloudStorage;

    @Mock
    private CloudStorageProperties cloudStorageProperties;

    @InjectMocks
    private CloudStorageService cloudStorageService;

    @Test
    public void chooseImageTest() {
        final List<CloudStorageProperties.Image> images =
            List.of(
                /* 0 */ new CloudStorageProperties.Image("bucket1", "name1"),
                /* 1 */ new CloudStorageProperties.Image("bucket2", "name2"),
                /* 2 */ new CloudStorageProperties.Image("bucket3", "name3")
            );
        final byte[] imageBytes = {};

        when(cloudStorageProperties.getImages()).thenReturn(images);
        when(random.nextInt(images.size())).thenReturn(2);
        when(cloudStorage.get("bucket3", "name3")).thenReturn(imageBytes);

        assertSame(imageBytes, cloudStorageService.chooseImage());

        verify(cloudStorageProperties).getImages();
        verify(random).nextInt(images.size());
        verify(cloudStorage).get("bucket3", "name3");
    }

    @Test
    public void chooseImageNullImagesTest() {
        when(cloudStorageProperties.getImages()).thenReturn(null);

        assertThatThrownBy(() -> cloudStorageService.chooseImage())
            .isInstanceOf(RuntimeException.class)
            .hasMessageStartingWith("`cloudstorage`.`images` should not be empty");

        verify(cloudStorageProperties).getImages();
    }

    @Test
    public void chooseImageEmptyImagesTest() {
        when(cloudStorageProperties.getImages()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> cloudStorageService.chooseImage())
            .isInstanceOf(RuntimeException.class)
            .hasMessageStartingWith("`cloudstorage`.`images` should not be empty");

        verify(cloudStorageProperties).getImages();
    }
}
