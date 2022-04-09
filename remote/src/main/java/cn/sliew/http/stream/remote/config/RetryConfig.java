package cn.sliew.http.stream.remote.config;

import io.github.resilience4j.core.EventConsumer;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.event.RetryOnErrorEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
public class RetryConfig {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public RegistryEventConsumer<Retry> customeLogRetryRegistryEventConsumer() {
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

    private void registerEventConsumer(Retry.EventPublisher eventPublisher) {
        eventPublisher.onError(new ErrorEventConsumer());
    }

    private class ErrorEventConsumer implements EventConsumer<RetryOnErrorEvent> {
        @Override
        public void consumeEvent(RetryOnErrorEvent event) {
            log.error("{} 重试失败! 失败次数: {}, 失败时间: {}",
                    event.getName(), event.getNumberOfRetryAttempts(),
                    event.getCreationTime().format(formatter),
                    event.getLastThrowable());
        }
    }
}
