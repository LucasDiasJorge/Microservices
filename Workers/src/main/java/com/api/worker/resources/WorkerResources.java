package com.api.worker.resources;

import com.api.worker.entities.Worker;
import com.api.worker.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import java.util.List;

@RestController
@RefreshScope
@RequestMapping(value = "/workers")
public class WorkerResources {

    private static final Logger logger = Logger.getLogger(WorkerResources.class.getName());

    @Value("${test.config}")
    private String value;

    @Autowired
    private Environment environment;

    @Autowired
    private WorkerRepository workerRepository;

    @GetMapping(value = "/configs")
    public ResponseEntity<Void> getConfigs(){
        logger.info("From port: " + environment.getProperty("server.port") + " Config value is: " + value);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Worker>> findAll(){
        List<Worker> list = workerRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Worker> findById(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(15000);
        Worker worker = workerRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(worker);
    }

}
