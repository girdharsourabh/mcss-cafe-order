package com.sourabh.cafeorder.service;

import com.sourabh.cafeorder.domain.CoffeeOrder;
import com.sourabh.cafeorder.model.CoffeeOrderDto;

import java.util.UUID;

public interface CoffeeOrderManager {

    CoffeeOrder newCoffeeOrder(CoffeeOrder coffeeOrder);

    void coffeeOrderPassedValidation(UUID coffeeOrderId);

    void coffeeOrderFailedValidation(UUID coffeeOrderId);

    void coffeeOrderAllocationPassed(CoffeeOrderDto coffeeOrder);

    void coffeeOrderAllocationPendingInventory(CoffeeOrderDto coffeeOrder);

    void coffeeOrderAllocationFailed(CoffeeOrderDto coffeeOrder);

    void pickupCoffeeOrder(UUID coffeeOrderId);

    void cancelOrder(UUID coffeeOrderId);
}
