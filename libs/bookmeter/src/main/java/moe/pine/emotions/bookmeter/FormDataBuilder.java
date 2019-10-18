package moe.pine.emotions.bookmeter;

import lombok.AccessLevel;
import lombok.Getter;
import moe.pine.emotions.springutils.NamedByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

@Getter(AccessLevel.PACKAGE)
class FormDataBuilder {
    private final String email;
    private final String password;

    FormDataBuilder(
        final String email,
        final String password
    ) {
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
    }

    MultiValueMap<String, String> buildLoginFormData(
        final String authenticityToken
    ) {
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("utf8", "✓");
        formData.add("authenticity_token", authenticityToken);
        formData.add("session[email_address]", email);
        formData.add("session[password]", password);
        formData.add("session[keep]", "0");
        formData.add("session[keep]", "1");

        return formData;
    }

    MultiValueMap<String, HttpEntity<?>> buildAccountFormData(
        final String authenticityToken,
        final String name,
        final byte[] image
    ) {
        final MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("utf8", "✓");
        builder.part("_method", "put");
        builder.part("authenticity_token", authenticityToken);
        builder.part("name", name);

        final Resource resource = new NamedByteArrayResource(image, "image.png");
        builder.part("icon", resource, MediaType.IMAGE_PNG);

        return builder.build();
    }
}
