package com.shoponline.warehouse;

import com.shoponline.warehouse.model.WarehouseItem;
import com.shoponline.warehouse.model.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/warehouse")
class WarehouseController {
    @Autowired
    private final WarehouseRepository repository;

    Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    WarehouseController(WarehouseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/items")
    public Iterable<WarehouseItem> all() {
        logger.info("----> Get all items");
        return repository.findAll();
    }

    @PostMapping("/items")
    public WarehouseItem addNewWarehouseItem(@RequestBody WarehouseItem item) {
        logger.info("----> Add new item: name {}, price {}, amount {}", item.getName(), item.getPrice(), item.getAmount());
        return repository.save(item);
    }

    @GetMapping("/items/{id}")
    public WarehouseItem one(@PathVariable Long id) {
        logger.info("----> Get item by id {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new WarehouseItemNotFoundException(id));
    }

    @PutMapping("/items/{id}/addition/{amount}")
    public WarehouseItem addExistingWarehouseItem(@PathVariable Long id, @PathVariable Integer amount) {
        logger.info("----> Change item {} amount by {}", id, amount);
        return repository.findById(id)
                .map(warehouseItem -> {
                    warehouseItem.setAmount(warehouseItem.getAmount() + amount);
                    return repository.save(warehouseItem);
                })
                .orElseThrow(() -> new WarehouseItemNotFoundException(id));
    }
}
