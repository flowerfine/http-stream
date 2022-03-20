package cn.sliew.http.stream.remote.config;

import io.github.resilience4j.core.EventConsumer;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.event.RateLimiterOnFailureEvent;
import io.github.resilience4j.retry.event.RetryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RateLimiterConfig {

    @Bean
    public RegistryEventConsumer<RateLimiter> customeRetryRegistryEventConsumer() {
        return new RegistryEventConsumer<>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<RateLimiter> entryAddedEvent) {
                registerEventConsumer(entryAddedEvent.getAddedEntry().getEventPublisher());
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<RateLimiter> entryRemoveEvent) {

            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<RateLimiter> entryReplacedEvent) {
                registerEventConsumer(entryReplacedEvent.getNewEntry().getEventPublisher());
            }
        };
    }

    @Bean
    public EventConsumer<RetryEvent> retryEventConsumer() {
        return event -> log.info("{}:----- {}", event.getClass().getName(), event.toString());
    }

    private void registerEventConsumer(RateLimiter.EventPublisher eventPublisher) {
        eventPublisher.onFailure(new FailureEventConsumer());
    }

    private class FailureEventConsumer implements EventConsumer<RateLimiterOnFailureEvent> {
        @Override
        public void consumeEvent(RateLimiterOnFailureEvent event) {
            log.error("event: {}", event);
        }
    }
}
