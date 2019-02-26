package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.cloudstorage.CloudStorage;
import moe.pine.emotions.properties.CloudStorageProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudStorageService {
    @NotNull
    private final CloudStorage cloudStorage;

    @NotNull
    private final CloudStorageProperties cloudStorageProperties;

    @NotNull
    public byte[] chooseImage() {
        final List<CloudStorageProperties.Image> images = cloudStorageProperties.getImages();

        final Random random = new Random();
        final int chosen = random.nextInt(images.size());
        final CloudStorageProperties.Image chosenImage = images.get(chosen);

        return cloudStorage.get(chosenImage.getBucket(), chosenImage.getName());
    }
}
