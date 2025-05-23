package kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;
import java.util.Scanner;

public class ProducerApplication {

    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Adjust for your Kafka broker
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        Thread.sleep(1000);

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Kafka topic: ");
        String topic = scanner.nextLine();

        while (true) {
            System.out.print("Enter message (or type 'exit' to quit): ");
            String message = scanner.nextLine();

            if ("exit".equalsIgnoreCase(message)) {
                break;
            }

            producer.send(new ProducerRecord<>(topic, message));
            System.out.println("Message sent to topic '" + topic + "': " + message);
        }

        producer.close();
        scanner.close();
    }


}
