package jtechlog.context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class ReactiveContextApplication {

    static void main() {
        new ReactiveContextApplication().run();
    }

    private void run() {
        Flux.range(0, 3)
                .flatMap(i -> processOrder())
                .subscribe();
    }

    private Mono<Void> processOrder() {
        String id = UUID.randomUUID().toString();
        log.info("process: {}", id);
        return saveOrder()
                .contextWrite(Context.of("requestId", id));
    }

    private Mono<Void> saveOrder() {
        return Mono.deferContextual(ctx -> {
            String id = ctx.get("requestId");
            log.info("save: {}", id);
            return Mono.empty();
        });
    }

}