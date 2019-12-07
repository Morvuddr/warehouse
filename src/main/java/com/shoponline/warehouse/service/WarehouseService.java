package com.shoponline.warehouse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoponline.warehouse.broker.RabbitMQ;
import com.shoponline.warehouse.dtos.ChangeStatusDTO;
import com.shoponline.warehouse.dtos.ReservedItemDTO;
import com.shoponline.warehouse.dtos.WarehouseItemDTO;
import com.shoponline.warehouse.model.reserved.ReservedItem;
import com.shoponline.warehouse.model.reserved.ReservedItemID;
import com.shoponline.warehouse.model.reserved.ReservedItemStatus;
import com.shoponline.warehouse.model.reserved.ReservedItemsRepository;
import com.shoponline.warehouse.model.warehouse.WarehouseItem;
import com.shoponline.warehouse.model.warehouse.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@EnableRabbit
@Service
public class WarehouseService {

    @Autowired
    private AmqpTemplate rabbitMQTemplate;

    @Autowired
    private final WarehouseRepository warehouseRepository;

    @Autowired
    private final ReservedItemsRepository reservedItemsRepository;

    private final Logger logger;
    private final ObjectMapper objectMapper;

    WarehouseService(WarehouseRepository warehouseRepository, ReservedItemsRepository reservedItemsRepository, ObjectMapper objectMapper) {
        this.warehouseRepository = warehouseRepository;
        this.reservedItemsRepository = reservedItemsRepository;
        this.objectMapper = objectMapper;
        this.logger = LoggerFactory.getLogger(WarehouseService.class);
    }

    public Iterable<WarehouseItem> findAll() {
        logger.info("----> Get all items");
        return warehouseRepository.findAll();
    }

    public WarehouseItem addNewItem(WarehouseItem item) {
        logger.info("----> Add new item: name {}, price {}, amount {}", item.getName(), item.getPrice(), item.getAmount());
        WarehouseItem addedItem = warehouseRepository.save(item);
        this.send(addedItem);
        return addedItem;
    }

    public WarehouseItem getItem(Integer id) {
        logger.info("----> Get item by id {}", id);
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new WarehouseItemNotFoundException(id));
    }

    private void send(WarehouseItem item) {
        WarehouseItemDTO itemDTO = new WarehouseItemDTO(item.getId(), item.getAmount(), item.getName(), item.getPrice());
        Message m = MessageBuilder.withBody(itemDTO.toString().getBytes()).build();
        rabbitMQTemplate.send(RabbitMQ.WAREHOUSE_EXCHANGE, RabbitMQ.WAREHOUSE_ROUTING_KEY, m);
        logger.info("----> Send message to rabbit with item {} ", item.getId());
    }

    @RabbitListener(queues = RabbitMQ.WAREHOUSE_QUEUE_RESERVE_ITEMS, errorHandler = "rabbitRetryHandler")
    private void reserveItem(byte[] bytes) {
        String json = new String(bytes);
        try {
            ReservedItemDTO itemDTO = objectMapper.readValue(json, ReservedItemDTO.class);
            logger.info("----> Received '" + itemDTO.toString() + "'");
            ReservedItem reservedItem = new ReservedItem(itemDTO.ItemId,itemDTO.OrderId,itemDTO.Amount);
            this.reserveItem(reservedItem);
        } catch (IOException e) {
            logger.info("----> Error" + e.getLocalizedMessage());
        }
    }

    @RabbitListener(queues = RabbitMQ.WAREHOUSE_QUEUE_STATUS, errorHandler = "rabbitRetryHandler")
    private void changeOrderStatus(byte[] bytes) {
        String json = new String(bytes);
        try {
            ChangeStatusDTO dto = objectMapper.readValue(json, ChangeStatusDTO.class);
            changeReservedItemStatus(dto.OrderId, dto.Status);
            logger.info("----> Received '" + dto.toString() + "'");
        } catch (IOException e) {
            logger.info("----> Error" + e.getLocalizedMessage());
        }
    }

    //@Transactional
    public WarehouseItem addExistingItem(Integer id, Integer amount) {
        logger.info("----> Change item {} amount by {}", id, amount);
        return warehouseRepository.findById(id)
                .map(warehouseItem -> {
                    warehouseItem.setAmount(warehouseItem.getAmount() + amount);
                    this.send(warehouseItem);
                    return warehouseRepository.save(warehouseItem);
                })
                .orElseThrow(() -> new WarehouseItemNotFoundException(id));
    }

    //@Transactional
    void reserveItem(ReservedItem reservedItem) {
        WarehouseItem warehouseItem = warehouseRepository
                .findById(reservedItem.getItemId())
                .orElseThrow(() -> new WarehouseItemNotFoundException(reservedItem.getItemId()));
        warehouseItem.setAmount(warehouseItem.getAmount() - reservedItem.getAmount());
        warehouseRepository.save(warehouseItem);
        Optional<ReservedItem> ri = reservedItemsRepository.findById(new ReservedItemID(reservedItem.getItemId(), reservedItem.getOrderId()));
        ri.ifPresentOrElse(i -> {
            i.setAmount(i.getAmount() + reservedItem.getAmount());
            reservedItemsRepository.save(i);
        }, ()-> reservedItemsRepository.save(reservedItem) );
    }

    //@Transactional
    void changeReservedItemStatus(Integer orderId, String status) {
        ReservedItemStatus newStatus = ReservedItemStatus.getEnumByString(status);
        reservedItemsRepository.findAllByOrderId(orderId).forEach(reservedItem -> {
            reservedItem.setStatus(newStatus);
            reservedItemsRepository.save(reservedItem);
            if (newStatus == ReservedItemStatus.FAILED || newStatus == ReservedItemStatus.CANCELLED) {
                warehouseRepository.findById(reservedItem.getItemId())
                        .map(warehouseItem -> {
                            warehouseItem.setAmount(warehouseItem.getAmount() + reservedItem.getAmount());
                            return warehouseRepository.save(warehouseItem);
                        });
            }
        });
    }

}
