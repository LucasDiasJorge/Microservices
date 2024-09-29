package com.api.worker.resources;

import com.api.worker.entities.Worker;
import com.api.worker.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import java.util.List;

@RestController
@RequestMapping(value = "/workers")
public class WorkerResources {

    private static final Logger logger = Logger.getLogger(WorkerResources.class.getName());

    @Autowired
    private Environment environment;

    @Autowired
    private WorkerRepository workerRepository;

    @GetMapping
    public ResponseEntity<List<Worker>> findAll(){
        List<Worker> list = workerRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Worker> findById(@PathVariable Long id){
        logger.info("Find worker by id: " + id + " From port: " + environment.getProperty("server.port"));
        Worker worker = workerRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(worker);
    }

}
