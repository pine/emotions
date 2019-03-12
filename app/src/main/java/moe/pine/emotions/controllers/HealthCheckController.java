package moe.pine.emotions.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HealthCheckController {
    @GetMapping("")
    public String home() {
        final String repositoryUrl = "https://github.com/pine/emotions";
        return String.format("redirect:%s", repositoryUrl);
    }

    @GetMapping(value = "health", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String health(HttpServletResponse response) {
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        response.addHeader(
            HttpHeaders.CACHE_CONTROL, "private, no-cache, no-store, must-revalidate");
        return "OK";
    }
}
