package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.cloudstorage.CloudStorage;
import moe.pine.emotions.properties.CloudStorageProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudStorageService {
    private final Random random;
    private final CloudStorage cloudStorage;
    private final CloudStorageProperties cloudStorageProperties;

    @SuppressWarnings("SpellCheckingInspection")
    public byte[] chooseImage() {
        var images = cloudStorageProperties.getImages();
        if (CollectionUtils.isEmpty(images)) {
            throw new RuntimeException("`cloudstorage`.`images` should not be empty");
        }

        int chosen = random.nextInt(images.size());
        var chosenImage = images.get(chosen);

        return cloudStorage.get(chosenImage.getBucket(), chosenImage.getName());
    }
}
