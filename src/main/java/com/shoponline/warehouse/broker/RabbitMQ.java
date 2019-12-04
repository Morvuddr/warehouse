package com.shoponline.warehouse.broker;

public class RabbitMQ {
    public static final String WAREHOUSE_EXCHANGE = "WarehouseService_SupplyItemsExchange";
    public static final String WAREHOUSE_ROUTING_KEY = "SupplyItems";

    public static final String ORDER_SERVICE_EXCHANGE_STATUS = "OrderService_ChangeOrderStatusExchange";
    public static final String WAREHOUSE_QUEUE_STATUS = "WarehouseQueueOrderStatus";

    public static final String ORDER_SERVICE_EXCHANGE_RESERVE_ITEMS = "OrderService_ReserveItemsExchange";
    public static final String WAREHOUSE_QUEUE_RESERVE_ITEMS = "WarehouseQueueReserveItems";
    public static final String ORDER_SERVICE_ROUTING_KEY_RESERVE_ITEMS = "ReserveItems";
}
