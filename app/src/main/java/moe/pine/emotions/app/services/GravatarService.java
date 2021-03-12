package moe.pine.emotions.app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.Gravatar;
import moe.pine.emotions.app.properties.GravatarProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GravatarService {
    private final Gravatar gravatar;
    private final GravatarProperties gravatarProperties;

    public void chooseImage() {
        final List<String> images = gravatarProperties.getImages();
        final List<String> addresses = gravatarProperties.getAddresses();
        gravatar.chooseImage(images, addresses);
    }
}
