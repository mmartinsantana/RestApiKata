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
}
