package moe.pine.emotions.controllers;

import lombok.SneakyThrows;
import moe.pine.emotions.properties.AppProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HealthControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppProperties appProperties;

    @Test
    @SneakyThrows
    public void homeTest() {
        when(appProperties.getSiteUrl()).thenReturn("https://www.example.com");

        mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("https://www.example.com"));
    }

    @Test
    @SneakyThrows
    public void homeTest_notFound() {
        when(appProperties.getSiteUrl()).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @SneakyThrows
    public void healthTest() {
        mvc.perform(MockMvcRequestBuilders.get("/health"))
            .andExpect(status().isOk())
            .andExpect(header().string("Pragma", "no-cache"))
            .andExpect(header()
                .string("Cache-Control", "private, no-cache, no-store, must-revalidate"))
            .andExpect(content().string("OK"));
    }
}
