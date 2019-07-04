package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.twitter.Twitter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwitterService {
    private final Twitter twitter;

    public void updateImage(final byte[] image) {
        checkArgument(ArrayUtils.isNotEmpty(image), "`image` should not be empty.");

        twitter.updateProfileImage(image);
    }
}
