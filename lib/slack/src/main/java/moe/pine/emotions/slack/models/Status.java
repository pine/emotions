package moe.pine.emotions.slack.models;

import lombok.Data;

@Data
public class Status {
    private boolean ok;
    private String error;
    private String warning;
    private String stuff;
}
