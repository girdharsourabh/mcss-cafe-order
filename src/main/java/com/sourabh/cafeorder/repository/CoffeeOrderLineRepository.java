package com.sourabh.cafeorder.repository;

import com.sourabh.cafeorder.domain.CoffeeOrderLine;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CoffeeOrderLineRepository extends PagingAndSortingRepository<CoffeeOrderLine, UUID> {
}
