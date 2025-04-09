package com.api.payroll.services;

import com.api.payroll.feignclients.WorkerFeignClient;
import com.api.payroll.entities.Payment;
import com.api.payroll.entities.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private WorkerFeignClient workerFeignClient;

    public ResponseEntity<?> getPayment(long workerId, int days) {
        try {
            ResponseEntity<Worker> response = workerFeignClient.findById(workerId);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Response inválida do serviço de Worker");
            }

            Worker worker = response.getBody();
            Payment payment = new Payment(worker.getName(), worker.getDailyIncome(), days);
            return ResponseEntity.ok(payment);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro ao tentar buscar Worker");
            errorResponse.put("details", e.getMessage());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(errorResponse);
        }
    }
}
