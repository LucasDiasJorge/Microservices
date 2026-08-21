package kafka.producer.controller;

import jakarta.validation.Valid;
import kafka.producer.dto.MessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Substitui o main() com Scanner lendo stdin, que não podia rodar
 * em container nem ser exercitado por testes.
 */
@RestController
@RequestMapping("/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public MessageController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<Map<String, Object>>> publish(@Valid @RequestBody MessageRequest request) {
        return kafkaTemplate.send(request.topic(), request.key(), request.message())
                .thenApply(this::toResponse);
    }

    private ResponseEntity<Map<String, Object>> toResponse(SendResult<String, String> result) {
        var metadata = result.getRecordMetadata();
        logger.info("Mensagem publicada em {}-{} offset {}",
                metadata.topic(), metadata.partition(), metadata.offset());
        return ResponseEntity.accepted().body(Map.of(
                "topic", metadata.topic(),
                "partition", metadata.partition(),
                "offset", metadata.offset()));
    }
}
