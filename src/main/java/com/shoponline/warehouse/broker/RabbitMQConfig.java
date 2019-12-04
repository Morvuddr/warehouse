package com.shoponline.warehouse.broker;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    DirectExchange warehouseExchange() {
        return new DirectExchange(RabbitMQ.WAREHOUSE_EXCHANGE, true, false);
    }

    @Bean
    FanoutExchange orderSeviceStatusExchange() {
        return new FanoutExchange(RabbitMQ.ORDER_SERVICE_EXCHANGE_STATUS, true, false);
    }
    @Bean
    DirectExchange orderServiceReserveItems() {
        return new DirectExchange(RabbitMQ.ORDER_SERVICE_EXCHANGE_RESERVE_ITEMS, true, false);
    }

    @Bean
    Queue queueStatus() {
        return new Queue(RabbitMQ.WAREHOUSE_QUEUE_STATUS, true);
    }

    @Bean
    Queue queueReservedItems() {
        return new Queue(RabbitMQ.WAREHOUSE_QUEUE_RESERVE_ITEMS, true);
    }

    @Bean
    Binding bindingStatus(Queue queueStatus, FanoutExchange orderSeviceStatusExchange) {
        return  BindingBuilder.bind(queueStatus)
                .to(orderSeviceStatusExchange);
    }

    @Bean
    Binding bindingReservedItems(Queue queueReservedItems, DirectExchange orderServiceReserveItems) {
        return BindingBuilder.bind(queueReservedItems)
                .to(orderServiceReserveItems)
                .with(RabbitMQ.ORDER_SERVICE_ROUTING_KEY_RESERVE_ITEMS);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public AmqpTemplate rabbitMQTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
