package com.shoponline.warehouse.model.reserved;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservedItemsRepository extends CrudRepository<ReservedItem, ReservedItemID> {

    List<ReservedItem> findAllByOrderId(Integer orderId);

}

