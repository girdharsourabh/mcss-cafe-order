package com.sourabh.cafeorder.mapper;

import com.sourabh.cafeorder.domain.Customer;
import com.sourabh.cafeorder.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface CustomerMapper {
    CustomerDto customerToDto(Customer customer);

    Customer dtoToCustomer(Customer dto);
}
