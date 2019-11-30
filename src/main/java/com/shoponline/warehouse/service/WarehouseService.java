package com.shoponline.warehouse.service;

import com.shoponline.warehouse.model.WarehouseItem;
import com.shoponline.warehouse.model.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@EnableRabbit
@Service
public class WarehouseService {

    @Autowired
    private AmqpTemplate rabbitMQTemplate;

    @Autowired
    private final WarehouseRepository repository;

    private final Logger logger;

    WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
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
                    return repository.save(warehouseItem);
                })
                .orElseThrow(() -> new WarehouseItemNotFoundException(id));
    }

    private void send(WarehouseItem item) {
        rabbitMQTemplate.convertAndSend("warehouse.exchange", "warehouse.routingkey", item);
        System.out.println("Send msg = " + item);
    }

    @RabbitListener(queues = "reserveitem.queue")
    private void reserveItem(WarehouseItem message) {
        System.out.println(" [x] Received '" + message.toString() + "'");
    }

    @RabbitListener(queues = "orderstatus.queue")
    private void changeItemStatus(WarehouseItem message) {
        System.out.println(" [x] Received '" + message.toString() + "'");
    }

}
