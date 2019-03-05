package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.Gravatar;
import moe.pine.emotions.gravatar.GravatarException;
import moe.pine.emotions.properties.GravatarProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GravatarService {
    @NotNull
    private final Gravatar gravatar;

    @NotNull
    private final GravatarProperties gravatarProperties;

    public void chooseImage() throws GravatarException {
        final List<String> images = gravatarProperties.getImages();
        final List<String> addresses = gravatarProperties.getAddresses();
        gravatar.chooseImage(images, addresses);
    }
}
