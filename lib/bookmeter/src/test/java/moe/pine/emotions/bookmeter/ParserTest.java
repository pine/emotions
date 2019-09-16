package moe.pine.emotions.bookmeter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@SuppressWarnings("NullableProblems")
public class ParserTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private Parser parser;

    @Test
    public void parseLoginFormTest_noForm() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Login form element not found.");

        parser.parseLoginForm("<html><body></body></html>");
    }

    @Test
    public void parseLoginFormTest_noAuthenticityToken() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Authenticity token element not found.");

        parser.parseLoginForm(
            "<html><body><div id=\"js_sessions_new_form\"><form></form></div></body></html>");
    }
}
