package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.twitter.Twitter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwitterService {
    @NotNull
    private final Twitter twitter;

    public void updateImage(@NotNull final byte[] image) {
        twitter.updateProfileImage(image);
    }
}
