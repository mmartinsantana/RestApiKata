package com.example.kata.api_rest.demo.message;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    private final RabbitTemplate rabbitTemplate;

    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String message) throws Exception {
        rabbitTemplate.convertAndSend(MessageConfig.TOPIC_NAME, "foo.bar.baz", message);

    }

    public void sendEphemeral(String message) throws Exception {
        rabbitTemplate.convertAndSend(MessageConfig.EPHEMERAL_EXCHANGE_NAME, "test_dead", message);

    }

    public String recover() throws Exception {
        Object msg = rabbitTemplate.receiveAndConvert(MessageConfig.DEAD_LETTER_QUEUE_NAME);

        if (msg != null) {
            System.out.println("Recovered!: ");
            return msg.toString();
        } else {
            System.out.println("Could not recover: ");
        }

        return null;
    }
}
