package com.sourabh.cafeorder.service;

import com.sourabh.cafeorder.model.CoffeeDto;
import com.sourabh.cafeorder.model.CoffeePagedList;

import java.util.Optional;
import java.util.UUID;

public interface CoffeeService {

    Optional<CoffeeDto> getCoffeeById(UUID uuid);

    Optional<CoffeeDto> getCoffeeByUpc(String upc);

    Optional<CoffeePagedList> getListofCoffees();
}
