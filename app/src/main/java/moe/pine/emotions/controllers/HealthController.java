package moe.pine.emotions.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.properties.AppProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HealthController {
    private final AppProperties appProperties;

    @GetMapping("")
    public void home(final HttpServletResponse response) throws IOException {
        final String siteUrl = appProperties.getSiteUrl();
        if (StringUtils.isEmpty(siteUrl)) {
            response.sendError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase());
            return;
        }

        response.sendRedirect(siteUrl);
    }

    @GetMapping(value = "health")
    @ResponseBody
    public String health() {
        return "OK";
    }
}
