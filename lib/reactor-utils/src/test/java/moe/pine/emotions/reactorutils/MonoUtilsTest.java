package moe.pine.emotions.reactorutils;

import lombok.SneakyThrows;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MonoUtilsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void unwrapTest() {
        final Mono<String> mono = mock(Mono.class);
        when(mono.block()).thenReturn("OK");

        assertEquals("OK", MonoUtils.unwrap(mono));

        verify(mono).block();
        verify(mono, never()).block(any());
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void unwrapTest_interruptedException() {
        expectedException.expect(InterruptedException.class);

        final Mono<String> mono = mock(Mono.class);
        final Exception e = Exceptions.propagate(new InterruptedException());
        when(mono.block()).thenThrow(e);

        MonoUtils.unwrap(mono);
    }


    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void unwrapTest_timeout() {
        final Mono<String> mono = mock(Mono.class);
        final Duration timeout = Duration.ofSeconds(1L);
        when(mono.block(timeout)).thenReturn("OK");

        assertEquals("OK", MonoUtils.unwrap(mono, timeout));

        verify(mono, never()).block();
        verify(mono).block(timeout);
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void unwrapTest_timeoutInterruptedException() {
        expectedException.expect(InterruptedException.class);

        final Mono<String> mono = mock(Mono.class);
        final Duration timeout = Duration.ofSeconds(1L);
        final Exception e = Exceptions.propagate(new InterruptedException());
        when(mono.block(timeout)).thenThrow(e);

        MonoUtils.unwrap(mono, timeout);
    }
}
