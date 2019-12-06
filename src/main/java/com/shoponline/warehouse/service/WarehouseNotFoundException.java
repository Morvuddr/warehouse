package com.shoponline.warehouse.service;

class WarehouseItemNotFoundException extends RuntimeException {

    public WarehouseItemNotFoundException(Integer id) {
        super("Could not find item by " + id);
    }

}
