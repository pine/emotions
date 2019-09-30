package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.bookmeter.Bookmeter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@RequiredArgsConstructor
public class BookmeterService {
    private final Bookmeter bookmeter;

    public void updateImage(final byte[] image) throws InterruptedException {
        checkArgument(ArrayUtils.isNotEmpty(image), "`image` should not be empty.");

        bookmeter.updateProfileImage(image);
    }
}
