package com.shoponline.warehouse.model;

import org.springframework.data.repository.CrudRepository;

public interface WarehouseRepository extends CrudRepository<WarehouseItem, Long> {
}

