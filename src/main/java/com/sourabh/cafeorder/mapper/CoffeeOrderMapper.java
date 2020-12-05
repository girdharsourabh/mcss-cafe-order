package com.sourabh.cafeorder.mapper;

import com.sourabh.cafeorder.domain.CoffeeOrder;
import com.sourabh.cafeorder.model.CoffeeOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {DateMapper.class, CoffeeOrderLineMapper.class})
public interface CoffeeOrderMapper {

    //@Mapping(source = "customer.id", target = "customerId")
    CoffeeOrderDto coffeeOrderToDto(CoffeeOrder coffeeOrder);

    CoffeeOrder dtoToCoffeeOrder(CoffeeOrderDto dto);

}
