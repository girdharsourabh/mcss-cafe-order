package com.sourabh.cafeorder.mapper;

import com.sourabh.cafeorder.domain.CoffeeOrderLine;
import com.sourabh.cafeorder.model.CoffeeOrderLineDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
@DecoratedWith(CoffeeOrderLineMapperDecorator.class)
public interface CoffeeOrderLineMapper {
    CoffeeOrderLineDto coffeeOrderLineToDto(CoffeeOrderLine line);

    CoffeeOrderLine dtoToCoffeeOrderLine(CoffeeOrderLineDto dto);

}