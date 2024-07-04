package com.example.springboot_rabbitmq_tutorial.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.json.queue.name}")
    private String jsonQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingkey;

    @Value("${rabbitmq.routing.json.key}")
    private String routingjsonkey;

    // Spring bean for RabbitMQ queue
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    // Spring bean for RabbitMQ JSON queue
    @Bean
    public Queue jsonQueue() {
        return new Queue(jsonQueue);
    }

    // Spring bean for RabbitMQ exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    // Binding between queue and exchange using routing key
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingkey);
    }

    // Binding between JSON queue and exchange using routing JSON key
    @Bean
    public Binding jsonBinding() {
        return BindingBuilder.bind(jsonQueue()).to(exchange()).with(routingjsonkey);
    }

    // Message converter for JSON messages
    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    // AmqpTemplate with JSON message converter
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
