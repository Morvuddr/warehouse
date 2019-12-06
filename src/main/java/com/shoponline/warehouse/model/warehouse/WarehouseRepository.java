package com.shoponline.warehouse.model.warehouse;

import org.springframework.data.repository.CrudRepository;

public interface WarehouseRepository extends CrudRepository<WarehouseItem, Integer> {
}

