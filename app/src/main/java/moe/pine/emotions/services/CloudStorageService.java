package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.cloudstorage.CloudStorage;
import moe.pine.emotions.properties.CloudStorageProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudStorageService {
    @Nonnull
    private final Random random;

    @Nonnull
    private final CloudStorage cloudStorage;

    @Nonnull
    private final CloudStorageProperties cloudStorageProperties;

    @Nonnull
    @SuppressWarnings("SpellCheckingInspection")
    public byte[] chooseImage() {
        final var images = cloudStorageProperties.getImages();
        if (CollectionUtils.isEmpty(images)) {
            throw new RuntimeException("`cloudstorage`.`images` should not be empty");
        }

        final int chosen = random.nextInt(images.size());
        final var chosenImage = images.get(chosen);

        return cloudStorage.get(chosenImage.getBucket(), chosenImage.getName());
    }
}
