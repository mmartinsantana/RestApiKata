package com.example.kata.jms_demo.jms_demo;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadMessageConfig {
    static final String EPHEMERAL_EXCHANGE_NAME = "ephemeral_exchange";
    static final String DEAD_LETTER_EXCHANGE_NAME = "dead_letter_exchange";

    static final String EPHEMERAL_QUEUE_NAME = "ephemeral_queue_name";
    static final String DEAD_LETTER_QUEUE_NAME = "dead_letter_queue_name";

    @Bean
    DirectExchange ephemeralExchange() {
        return new DirectExchange(EPHEMERAL_EXCHANGE_NAME);
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE_NAME);
    }

    @Bean
    Queue ephemeralQueue() {
        return QueueBuilder
                .durable(EPHEMERAL_QUEUE_NAME)
                .ttl(5000)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", "dead_letter")
                .build();
    }

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE_NAME)
                .build();
    }

    @Bean
    Binding ephemeralBinding() {
        return BindingBuilder.bind(ephemeralQueue()).to(ephemeralExchange()).with("test_dead");
    }

    @Bean
    Binding deadLetterQueueBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("dead_letter");
    }
/*
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }
*/
   /* @Bean
    SimpleMessageListenerContainer containerTwo(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapterExploding) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(EPHEMERAL_QUEUE_NAME, DEAD_LETTER_QUEUE_NAME);
        container.setMessageListener(listenerAdapterExploding);
        return container;
    }*/

    @Bean
    MessageListenerAdapter listenerAdapterExploding(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
