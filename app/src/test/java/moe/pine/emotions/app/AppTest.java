package moe.pine.emotions.app;

import moe.pine.emotions.cloudstorage.CloudStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {
    @Autowired
    private App app;

    @MockBean
    private CloudStorage cloudStorage;

    @Test
    public void mainTest() {
        assertNotNull(app);
    }
}
