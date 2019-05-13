package moe.pine.emotions.services;

import moe.pine.emotions.models.Metric;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class MackerelService {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void send(@Nonnull final List<Metric> metrics) {
        checkNotNull(metrics);

        if (CollectionUtils.isEmpty(metrics)) {
            return;
        }
    }
}
