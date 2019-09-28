package moe.pine.emotions.reactorutils;

import lombok.experimental.UtilityClass;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.time.Duration;

@UtilityClass
public class MonoUtils {
    public <T> T unwrap(final Mono<T> mono) throws InterruptedException {
        try {
            return mono.block();
        } catch (Exception e) {
            final Throwable unwrapped = Exceptions.unwrap(e);
            if (unwrapped instanceof InterruptedException) {
                throw (InterruptedException) unwrapped;
            }
            throw e;
        }
    }

    public <T> T unwrap(final Mono<T> mono, final Duration timeout) throws InterruptedException {
        try {
            return mono.block(timeout);
        } catch (Exception e) {
            final Throwable unwrapped = Exceptions.unwrap(e);
            if (unwrapped instanceof InterruptedException) {
                throw (InterruptedException) unwrapped;
            }
            throw e;
        }
    }
}
