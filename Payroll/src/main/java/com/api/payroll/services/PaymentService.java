package com.api.payroll.services;

import com.api.payroll.feignclients.WorkerFeignClient;
import com.api.payroll.entities.Payment;
import com.api.payroll.entities.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

@Service
public class PaymentService {

    @Autowired
    private WorkerFeignClient workerFeignClient;

    public Payment getPayment(long workerId, int days) {
        ResponseEntity<Worker> response = workerFeignClient.findById(workerId);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return null;
        }

        Worker worker = response.getBody();

        return new Payment(worker.getName(), worker.getDailyIncome(), days);
    }
}
