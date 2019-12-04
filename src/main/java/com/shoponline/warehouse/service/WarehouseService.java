package com.shoponline.warehouse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoponline.warehouse.broker.RabbitMQ;
import com.shoponline.warehouse.dtos.ReservedItemDTO;
import com.shoponline.warehouse.dtos.WarehouseItemDTO;
import com.shoponline.warehouse.model.ReservedItem;
import com.shoponline.warehouse.model.WarehouseItem;
import com.shoponline.warehouse.model.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@EnableRabbit
@Service
public class WarehouseService {

    @Autowired
    private AmqpTemplate rabbitMQTemplate;

    @Autowired
    private final WarehouseRepository repository;

    private final Logger logger;
    private final ObjectMapper objectMapper;

    WarehouseService(WarehouseRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.logger = LoggerFactory.getLogger(WarehouseService.class);
    }

    public Iterable<WarehouseItem> findAll() {
        logger.info("----> Get all items");
        return repository.findAll();
    }

    public WarehouseItem addNewItem(WarehouseItem item) {
        logger.info("----> Add new item: name {}, price {}, amount {}", item.getName(), item.getPrice(), item.getAmount());
        WarehouseItem addedItem = repository.save(item);
        this.send(addedItem);
        return addedItem;
    }

    public WarehouseItem getItem(Long id) {
        logger.info("----> Get item by id {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new WarehouseItemNotFoundException(id));
    }

    public WarehouseItem addExistingItem(Long id, Integer amount) {
        logger.info("----> Change item {} amount by {}", id, amount);
        return repository.findById(id)
                .map(warehouseItem -> {
                    warehouseItem.setAmount(warehouseItem.getAmount() + amount);
                    this.send(warehouseItem);
                    return repository.save(warehouseItem);
                })
                .orElseThrow(() -> new WarehouseItemNotFoundException(id));
    }

    private void send(WarehouseItem item) {
        WarehouseItemDTO itemDTO = new WarehouseItemDTO(item.getId(), item.getAmount(), item.getName(), item.getPrice());
        Message m = MessageBuilder.withBody(itemDTO.toString().getBytes()).build();
        rabbitMQTemplate.send(RabbitMQ.WAREHOUSE_EXCHANGE, RabbitMQ.WAREHOUSE_ROUTING_KEY, m);
        logger.info("----> Send message to rabbit with item {} ", item.getId());
    }

    @RabbitListener(queues = RabbitMQ.WAREHOUSE_QUEUE_RESERVE_ITEMS)
    private void reserveItem(byte[] bytes) {
        String json = new String(bytes);
        ReservedItemDTO item;
        try {
            item = objectMapper.readValue(json, ReservedItemDTO.class);
            logger.info("----> Received '" + item.toString() + "'");
        } catch (IOException e) {
            logger.info("----> Error" + e.getLocalizedMessage());
        }
    }
//
//    @RabbitListener(queues = RabbitMQ.WAREHOUSE_QUEUE_STATUS)
//    private void changeOrgerStatus(String message) {
//        System.out.println(" [x] Received '" + message + "'");
//    }

}
