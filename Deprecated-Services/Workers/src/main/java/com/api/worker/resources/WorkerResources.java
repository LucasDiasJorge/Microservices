package com.api.worker.resources;

import com.api.worker.entities.Worker;
import com.api.worker.repositories.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RefreshScope
@RestController
@RequestMapping(value = "/workers")
public class WorkerResources {

    private static final Logger logger = LoggerFactory.getLogger(WorkerResources.class);

    private final Environment environment;
    private final WorkerRepository workerRepository;

    /** Vem do Config Server; default evita que a aplicação falhe ao subir sem ele. */
    @Value("${test.config:<sem config server>}")
    private String testConfig;

    /**
     * Atraso artificial usado para demonstrar o circuit breaker do gateway.
     * Zero por padrão — antes era um Thread.sleep(15000) fixo no findById.
     */
    @Value("${worker.demo.delay-ms:0}")
    private long demoDelayMs;

    public WorkerResources(Environment environment, WorkerRepository workerRepository) {
        this.environment = environment;
        this.workerRepository = workerRepository;
    }

    @GetMapping(value = "/configs")
    public ResponseEntity<Void> getConfigs() {
        logger.info("Porta {} | test.config = {}", environment.getProperty("local.server.port"), testConfig);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Worker>> findAll() {
        return ResponseEntity.ok(workerRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Worker> findById(@PathVariable Long id) throws InterruptedException {
        if (demoDelayMs > 0) {
            Thread.sleep(demoDelayMs);
        }
        return workerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
