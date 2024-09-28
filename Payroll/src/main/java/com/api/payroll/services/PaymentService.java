package com.api.payroll.services;

import com.api.payroll.WorkerFeignClient;
import com.api.payroll.entities.Payment;
import com.api.payroll.entities.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private WorkerFeignClient workerFeignClient;

    public Payment getPayment(long workerId, int days) {

        Worker worker = workerFeignClient.findById(workerId).getBody();

        if (worker == null) {
            return null;
        }

        return new Payment(worker.getName(),  worker.getDailyIncome(), days);
    }
}