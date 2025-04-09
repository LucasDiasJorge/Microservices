package com.api.payroll.feignclients;

import com.api.payroll.entities.Worker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "worker")
public interface WorkerFeignClient {

    @GetMapping("/workers/{id}")
    ResponseEntity<Worker> findById(@PathVariable Long id);
}
