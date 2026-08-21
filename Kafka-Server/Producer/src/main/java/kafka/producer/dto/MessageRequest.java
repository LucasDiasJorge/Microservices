package kafka.producer.dto;

import jakarta.validation.constraints.NotBlank;

public record MessageRequest(@NotBlank String topic, String key, @NotBlank String message) {
}
