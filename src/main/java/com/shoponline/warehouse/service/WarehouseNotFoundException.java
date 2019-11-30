package com.shoponline.warehouse.service;

class WarehouseItemNotFoundException extends RuntimeException {

    public WarehouseItemNotFoundException(Long id) {
        super("Could not find item by " + id);
    }

}
