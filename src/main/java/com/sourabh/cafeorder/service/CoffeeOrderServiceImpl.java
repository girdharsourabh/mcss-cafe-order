package com.sourabh.cafeorder.service;
import com.sourabh.cafeorder.domain.CoffeeOrder;
import com.sourabh.cafeorder.domain.CoffeeOrderStatusEnum;
import com.sourabh.cafeorder.domain.Customer;
import com.sourabh.cafeorder.mapper.CoffeeOrderMapper;
import com.sourabh.cafeorder.model.CoffeeOrderDto;
import com.sourabh.cafeorder.model.CoffeeOrderPagedList;
import com.sourabh.cafeorder.repository.CoffeeOrderRepository;
import com.sourabh.cafeorder.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoffeeOrderServiceImpl implements CoffeeOrderService {

    private final CoffeeOrderRepository coffeeOrderRepository;
    private final CustomerRepository customerRepository;
    private final CoffeeOrderMapper coffeeOrderMapper;
    private final CoffeeOrderManager coffeeOrderManager;

    @Override
    public CoffeeOrderPagedList listOrders(UUID customerId, Pageable pageable) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Page<CoffeeOrder> coffeeOrderPage =
                    coffeeOrderRepository.findAllByCustomer(customerOptional.get(), pageable);

            return new CoffeeOrderPagedList(coffeeOrderPage
                    .stream()
                    .map(coffeeOrderMapper::coffeeOrderToDto)
                    .collect(Collectors.toList()), PageRequest.of(
                    coffeeOrderPage.getPageable().getPageNumber(),
                    coffeeOrderPage.getPageable().getPageSize()),
                    coffeeOrderPage.getTotalElements());
        } else {
            return null;
        }
    }

    @Override
    public CoffeeOrderDto placeOrder(UUID customerId, CoffeeOrderDto coffeeOrderDto) {

        log.debug("Placing Order " + coffeeOrderDto.toString());

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found. UUID: " + customerId));

        CoffeeOrder coffeeOrder = coffeeOrderMapper.dtoToCoffeeOrder(coffeeOrderDto);
        coffeeOrder.setId(null); //should not be set by outside client
        coffeeOrder.setCustomer(customer);
        coffeeOrder.setOrderStatus(CoffeeOrderStatusEnum.NEW);

        CoffeeOrder savedCoffeeOrder = coffeeOrderRepository.save(coffeeOrder);

        log.debug("Saved Coffee Order: " + coffeeOrder.getId());

        coffeeOrderManager.newCoffeeOrder(savedCoffeeOrder);

        return coffeeOrderMapper.coffeeOrderToDto(savedCoffeeOrder);
    }

    @Override
    public CoffeeOrderDto getOrderById(UUID customerId, UUID orderId) {
        return coffeeOrderMapper.coffeeOrderToDto(getOrder(customerId, orderId));
    }

    @Override
    public void pickupOrder(UUID customerId, UUID orderId) {
        CoffeeOrder coffeeOrder = getOrder(customerId, orderId);
        coffeeOrder.setOrderStatus(CoffeeOrderStatusEnum.PICKED_UP);

        coffeeOrderRepository.save(coffeeOrder);
    }

    private CoffeeOrder getOrder(UUID customerId, UUID orderId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Customer Not Found. UUID: " + customerId));

        CoffeeOrder coffeeOrder = coffeeOrderRepository
                .findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "CoffeeOrder Not Found. UUID: " + orderId));

        // fall to exception if customer id's do not match - order not for customer
        if (coffeeOrder.getCustomer().equals(customer)) {
            return coffeeOrder;
        } else {
            throw new RuntimeException("Customer Not Found");
        }
    }


}