package moe.pine.emotions.slack.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    private boolean ok;

    @Nullable
    private String error;

    @Nullable
    private String warning;

    @Nullable
    private String stuff;
}
