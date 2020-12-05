package com.sourabh.cafeorder.service;

import com.sourabh.cafeorder.model.CustomerPagedList;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerPagedList listCustomers(Pageable pageable);

}
