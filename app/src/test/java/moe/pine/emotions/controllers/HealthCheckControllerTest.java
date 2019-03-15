package moe.pine.emotions.controllers;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HealthCheckControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    @SneakyThrows
    public void homeTest() {
        mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(HealthCheckController.REPOSITORY_URL));
    }

    @Test
    @SneakyThrows
    public void healthTest() {
        mvc.perform(MockMvcRequestBuilders.get("/health"))
            .andExpect(status().isOk())
            .andExpect(header().string("Pragma", "no-cache"))
            .andExpect(header().string("Cache-Control", "private, no-cache, no-store, must-revalidate"))
            .andExpect(content().string("OK"));
    }
}
