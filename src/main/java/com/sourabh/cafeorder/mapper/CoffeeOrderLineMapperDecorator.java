package com.sourabh.cafeorder.mapper;

import com.sourabh.cafeorder.domain.CoffeeOrderLine;
import com.sourabh.cafeorder.model.CoffeeDto;
import com.sourabh.cafeorder.model.CoffeeOrderLineDto;
import com.sourabh.cafeorder.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

public abstract class CoffeeOrderLineMapperDecorator implements CoffeeOrderLineMapper {

    private CoffeeService coffeeService;
    private CoffeeOrderLineMapper coffeeOrderLineMapper;

    @Autowired
    public void setCoffeeService(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @Autowired
    @Qualifier("delegate")
    public void setCoffeeOrderLineMapper(CoffeeOrderLineMapper coffeeOrderLineMapper) {
        this.coffeeOrderLineMapper = coffeeOrderLineMapper;
    }

    @Override
    public CoffeeOrderLineDto coffeeOrderLineToDto(CoffeeOrderLine line) {
        CoffeeOrderLineDto orderLineDto = coffeeOrderLineMapper.coffeeOrderLineToDto(line);
        Optional<CoffeeDto> coffeeDtoOptional = coffeeService.getCoffeeById(line.getCoffeeId());

        coffeeDtoOptional.ifPresent(coffeeDto -> {
            orderLineDto.setCoffeeName(coffeeDto.getCoffeeName());
            orderLineDto.setCoffeeStyle(coffeeDto.getCoffeeName());
            orderLineDto.setUpc(coffeeDto.getUpc());
            orderLineDto.setPrice(coffeeDto.getPrice());
        });

        return orderLineDto;
    }
}
