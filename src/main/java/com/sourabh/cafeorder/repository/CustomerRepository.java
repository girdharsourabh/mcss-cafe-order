package com.sourabh.cafeorder.repository;

import com.sourabh.cafeorder.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findAllByCustomerNameLike(String customerName);

    Optional<Customer> findByCustomerName(String name);
}
