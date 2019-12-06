package com.shoponline.warehouse.controller;

import com.shoponline.warehouse.service.WarehouseService;
import com.shoponline.warehouse.model.warehouse.WarehouseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/warehouse")
class WarehouseController {

    @Autowired
    private final WarehouseService service;

    WarehouseController(WarehouseService service) {
        this.service = service;
    }

    @GetMapping("/items")
    public Iterable<WarehouseItem> all() {
        return service.findAll();
    }

    @PostMapping("/items")
    public WarehouseItem addNewWarehouseItem(@RequestBody WarehouseItem item) {
        return service.addNewItem(item);
    }

    @GetMapping("/items/{id}")
    public WarehouseItem one(@PathVariable Integer id) {
        return service.getItem(id);
    }

    @PutMapping("/items/{id}/addition/{amount}")
    public WarehouseItem addExistingWarehouseItem(@PathVariable Integer id, @PathVariable Integer amount) {
        return service.addExistingItem(id, amount);
    }

}
