package moe.pine.emotions.bookmeter;

import moe.pine.emotions.spring_utils.NamedByteArrayResource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("NullableProblems")
public class FormDataBuilderTest {
    private static final String EMAIL = "example@gmail.com";
    private static final String PASSWORD = "password";

    private FormDataBuilder formDataBuilder;

    @Before
    public void setUp() {
        formDataBuilder = new FormDataBuilder(EMAIL, PASSWORD);
    }

    @Test
    public void constructorTest() {
        final var formDataBuilder = new FormDataBuilder(EMAIL, PASSWORD);
        assertEquals(EMAIL, formDataBuilder.getEmail());
        assertEquals(PASSWORD, formDataBuilder.getPassword());
    }

    @Test
    public void buildLoginFormDataTest() {
        final MultiValueMap<String, String> expected = new LinkedMultiValueMap<>();
        expected.add("utf8", "✓");
        expected.add("authenticity_token", "authenticity_token");
        expected.add("session[email_address]", EMAIL);
        expected.add("session[password]", PASSWORD);
        expected.add("session[keep]", "0");
        expected.add("session[keep]", "1");

        final var actual = formDataBuilder.buildLoginFormData("authenticity_token");
        assertEquals(expected, actual);
    }

    @Test
    public void buildAccountFormDataTest() {
        final byte[] image = new byte[]{0x01, 0x02, 0x03};
        final Resource resource = new NamedByteArrayResource(image, "image.png");
        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);

        final MultiValueMap<String, HttpEntity<?>> expected = new LinkedMultiValueMap<>();
        expected.add("utf8", new HttpEntity<>("✓"));
        expected.add("_method", new HttpEntity<>("put"));
        expected.add("authenticity_token", new HttpEntity<>("authenticity_token"));
        expected.add("name", new HttpEntity<>("homu"));
        expected.add("icon", new HttpEntity<>(resource, headers));

        final var actual = formDataBuilder
            .buildAccountFormData("authenticity_token", "homu", image);
        assertEquals(expected, actual);
    }
}
