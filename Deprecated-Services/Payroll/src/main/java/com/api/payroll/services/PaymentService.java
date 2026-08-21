package com.api.payroll.services;

import com.api.payroll.entities.Payment;
import com.api.payroll.entities.Worker;
import com.api.payroll.feignclients.WorkerFeignClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final WorkerFeignClient workerFeignClient;

    public PaymentService(WorkerFeignClient workerFeignClient) {
        this.workerFeignClient = workerFeignClient;
    }

    public Payment getPayment(long workerId, int days) {
        ResponseEntity<Worker> response;
        try {
            response = workerFeignClient.findById(workerId);
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker " + workerId + " não encontrado", e);
        } catch (FeignException e) {
            // Cobre o caso em que o Worker ainda não foi descoberto pelo load balancer
            // ou está fora do ar: o Feign lança em vez de devolver a ResponseEntity.
            logger.warn("Falha ao consultar o serviço de Worker: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de Worker indisponível", e);
        }

        Worker worker = response.getBody();
        if (!response.getStatusCode().is2xxSuccessful() || worker == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Resposta inválida do serviço de Worker");
        }

        return new Payment(worker.getName(), worker.getDailyIncome(), days);
    }
}
