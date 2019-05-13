package moe.pine.emotions.models;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Metric {
    private String name;
    private Long time;
    private BigDecimal value;
}
