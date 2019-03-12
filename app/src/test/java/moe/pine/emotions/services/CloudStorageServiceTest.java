package moe.pine.emotions.services;

import com.google.common.collect.ImmutableList;
import moe.pine.emotions.cloudstorage.CloudStorage;
import moe.pine.emotions.properties.CloudStorageProperties;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"SpellCheckingInspection", "ResultOfMethodCallIgnored"})
public class CloudStorageServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    Random random;

    @Mock
    CloudStorage cloudStorage;

    @Mock
    CloudStorageProperties cloudStorageProperties;

    @InjectMocks
    CloudStorageService cloudStorageService;

    @Test
    public void chooseImageTest() {
        final List<CloudStorageProperties.Image> images =
            ImmutableList.of(
                /* 0 */ new CloudStorageProperties.Image("bucket1", "name1"),
                /* 1 */ new CloudStorageProperties.Image("bucket2", "name2"),
                /* 2 */ new CloudStorageProperties.Image("bucket3", "name3")
            );
        final byte[] imageBytes = new byte[]{};

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
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("`cloudstorage`.`images` should not be empty");

        when(cloudStorageProperties.getImages()).thenReturn(null);

        cloudStorageService.chooseImage();

        verify(cloudStorageProperties).getImages();
    }

    @Test
    public void chooseImageEmptyImagesTest() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("`cloudstorage`.`images` should not be empty");

        when(cloudStorageProperties.getImages()).thenReturn(Collections.emptyList());

        cloudStorageService.chooseImage();

        verify(cloudStorageProperties).getImages();
    }
}
