package cn.sliew.http.stream.remote.config;

import io.github.resilience4j.core.EventConsumer;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RetryConfig {

    @Bean
    public RegistryEventConsumer<Retry> customeRetryRegistryEventConsumer() {
        return new RegistryEventConsumer<>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
                registerEventConsumer(entryAddedEvent.getAddedEntry().getEventPublisher());
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {

            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
                registerEventConsumer(entryReplacedEvent.getNewEntry().getEventPublisher());
            }
        };
    }

    @Bean
    public EventConsumer<RetryEvent> retryEventConsumer() {
        return event -> log.info("{}:----- {}", event.getClass().getName(), event.toString());
    }

    private void registerEventConsumer(Retry.EventPublisher eventPublisher) {
        eventPublisher.onRetry(new RetryEventConsumer());
        eventPublisher.onSuccess(new SuccessEventConsumer());
        eventPublisher.onError(new ErrorEventConsumer());
        eventPublisher.onIgnoredError(new IgnoredErrorEventConsumer());
    }

    private class IgnoredErrorEventConsumer implements EventConsumer<RetryOnIgnoredErrorEvent> {
        @Override
        public void consumeEvent(RetryOnIgnoredErrorEvent event) {
            log.error("event: {}", event);
        }
    }

    private class ErrorEventConsumer implements EventConsumer<RetryOnErrorEvent> {
        @Override
        public void consumeEvent(RetryOnErrorEvent event) {
            log.error("event: {}", event);
        }
    }

    private class RetryEventConsumer implements EventConsumer<RetryOnRetryEvent> {
        @Override
        public void consumeEvent(RetryOnRetryEvent event) {
            log.error("event: {}", event);
        }
    }

    private class SuccessEventConsumer implements EventConsumer<RetryOnSuccessEvent> {
        @Override
        public void consumeEvent(RetryOnSuccessEvent event) {
            log.error("event: {}", event);
        }
    }
}
