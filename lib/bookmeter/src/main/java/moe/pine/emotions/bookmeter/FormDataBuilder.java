package moe.pine.emotions.bookmeter;

import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
class FormDataBuilder {
    private final String email;
    private final String password;

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
}
