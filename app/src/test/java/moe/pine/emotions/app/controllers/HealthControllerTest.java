package moe.pine.emotions.app.controllers;

import moe.pine.emotions.app.properties.AppProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HealthControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppProperties appProperties;

    @Test
    public void homeTest() throws Exception {
        when(appProperties.getSiteUrl()).thenReturn("https://www.example.com");

        mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("https://www.example.com"));

        verify(appProperties).getSiteUrl();
    }

    @Test
    public void homeTest_notFound() throws Exception {
        when(appProperties.getSiteUrl()).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

        verify(appProperties).getSiteUrl();
    }

    @Test
    public void healthTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/health"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("OK")));
    }
}
