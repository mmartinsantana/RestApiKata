package com.example.kata.api_rest.demo.message;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {
    static final String TOPIC_NAME = "general_topic_name";

    static final String EPHEMERAL_EXCHANGE_NAME = "ephemeral_exchange";
    static final String DEAD_LETTER_QUEUE_NAME = "dead_letter_queue_name";

    static final String QUEUE_NAME = "general_queue_name";
/*
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }*/
}
