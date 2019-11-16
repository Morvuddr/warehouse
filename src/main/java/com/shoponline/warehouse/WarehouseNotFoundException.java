package com.shoponline.warehouse;

class WarehouseItemNotFoundException extends RuntimeException {

    WarehouseItemNotFoundException(Long id) {
        super("Could not find item by " + id);
    }

}
