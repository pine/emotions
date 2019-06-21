package moe.pine.emotions.slack.models;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class Status {
    private boolean ok;

    @Nullable
    private String error;

    @Nullable
    private String warning;

    @Nullable
    private String stuff;
}
