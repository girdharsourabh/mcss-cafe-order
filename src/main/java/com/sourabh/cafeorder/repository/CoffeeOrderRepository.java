package com.sourabh.cafeorder.repository;

import com.sourabh.cafeorder.domain.CoffeeOrder;
import com.sourabh.cafeorder.domain.CoffeeOrderStatusEnum;
import com.sourabh.cafeorder.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CoffeeOrderRepository  extends JpaRepository<CoffeeOrder, UUID> {

    Page<CoffeeOrder> findAllByCustomer(Customer customer, Pageable pageable);

    List<CoffeeOrder> findAllByOrderStatus(CoffeeOrderStatusEnum orderStatusEnum);

    @Query("select b from CoffeeOrder b where b.id = ?1")
    Optional<CoffeeOrder> findOrderUsingStringId(String id);
}
