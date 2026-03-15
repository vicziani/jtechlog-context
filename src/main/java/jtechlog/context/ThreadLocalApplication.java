package jtechlog.context;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
public class ThreadLocalApplication {

    private final Random random = new Random();

    private final ThreadLocal<String> requestId = new ThreadLocal<>();

    static void main() {
        new ThreadLocalApplication().run();
    }

    @SneakyThrows
    private void run() {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            executor.invokeAll(IntStream.range(0, 3)
                    .mapToObj(i -> Executors.callable(this::processOrder))
                    .toList()
            );
        }
    }

    @SneakyThrows
    private void processOrder() {
        String id = UUID.randomUUID().toString();
        log.info("process: {} {}", id, requestId.get());
        requestId.set(id);
        Thread.sleep(random.nextInt(1000));
        saveOrder();
    }

    private void saveOrder() {
        String id = requestId.get();
        log.info("save: {}", id);
        requestId.remove();
    }

}
