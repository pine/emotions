package moe.pine.emotions.mackerel;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Metric {
    String name;
    long time;
    BigDecimal value;
}
