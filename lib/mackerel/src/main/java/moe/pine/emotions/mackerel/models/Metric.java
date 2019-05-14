package moe.pine.emotions.mackerel.models;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Metric {
    private String name;
    private Long time;
    private BigDecimal value;
}
