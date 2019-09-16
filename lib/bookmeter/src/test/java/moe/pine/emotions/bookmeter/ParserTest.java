package moe.pine.emotions.bookmeter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("NullableProblems")
public class ParserTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private Parser parser;

    @Test
    public void parseLoginFormTest() {
        final String token =
            parser.parseLoginForm(
                "<html><body><div id=\"js_sessions_new_form\"><form>"
                    + "<input name=\"authenticity_token\" value=\"TOKEN\"></form></div></body></html>");

        assertEquals("TOKEN", token);
    }

    @Test
    public void parseLoginFormTest_noFormElement() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Login form element not found.");

        parser.parseLoginForm("<html><body></body></html>");
    }

    @Test
    public void parseLoginFormTest_noAuthenticityTokenElement() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Authenticity token element not found.");

        parser.parseLoginForm(
            "<html><body><div id=\"js_sessions_new_form\"><form></form></div></body></html>");
    }

    @Test
    public void parseLoginFormTest_noAuthenticityTokenValue() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("`authenticity_token` is not found.");

        parser.parseLoginForm(
            "<html><body><div id=\"js_sessions_new_form\"><form>"
                + "<input name=\"authenticity_token\" value=\"\"></form></div></body></html>");
    }

    @Test
    public void parseAccountFormTest_noFormElement() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Account form element not found.");

        parser.parseAccountForm("<html><body></body></html>");
    }

    @Test
    public void parseAccountFormTest_noNameElement() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Name element not found.");

        parser.parseAccountForm(
            "<html><body><form id=\"js_account_form\">"
                + "</form></body></html>");
    }

    @Test
    public void parseAccountFormTest_noNameValue() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("`name` is not found.");

        parser.parseAccountForm(
            "<html><body><form id=\"js_account_form\">"
                + "<input name=\"name\" value=\"\">"
                + "</form></body></html>");
    }

    @Test
    public void parseAccountFormTest_noAuthenticityTokenElement() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Authenticity token element not found.");

        parser.parseAccountForm(
            "<html><body><form id=\"js_account_form\">"
                + "<input name=\"name\" value=\"NAME\">"
                + "</form></body></html>");
    }

    @Test
    public void parseAccountFormTest_noAuthenticityTokenValue() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("`authenticity_token` is not found.");

        parser.parseAccountForm(
            "<html><body><form id=\"js_account_form\">"
                + "<input name=\"name\" value=\"NAME\">"
                + "<input name=\"authenticity_token\" value=\"\">"
                + "</form></body></html>");
    }
}
