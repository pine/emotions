package moe.pine.emotions.reactorutils;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.time.Duration;

@UtilityClass
public class MonoUtils {
    public <T> T unwrap(final Mono<T> mono) throws InterruptedException {
        try {
            return mono.block();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof InterruptedException) {
                throw (InterruptedException) e.getCause();
            }
            throw e;
        }
    }

    public <T> T unwrap(final Mono<T> mono, final Duration timeout) throws InterruptedException {
        try {
            return mono.block(timeout);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof InterruptedException) {
                throw (InterruptedException) e.getCause();
            }
            throw e;
        }
    }
}
