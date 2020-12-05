package com.sourabh.cafeorder.service;

import com.sourabh.cafeorder.config.CoffeeOrderStartup;
import com.sourabh.cafeorder.domain.CoffeeOrder;
import com.sourabh.cafeorder.domain.CoffeeOrderLine;
import com.sourabh.cafeorder.domain.Customer;
import com.sourabh.cafeorder.model.CoffeeDto;
import com.sourabh.cafeorder.model.CoffeePagedList;
import com.sourabh.cafeorder.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TastingService {
    private final CoffeeOrderManager coffeeOrderManager;
    private final CustomerRepository customerRepository;
    private final CoffeeService coffeeService;

    @Scheduled(fixedRateString = "${sfg.tasting.room.rate}")
    public void createTastingRoomOrder(){

        getRandomCoffee().ifPresent(coffeeId -> {

            Customer customer = customerRepository.findByCustomerName(CoffeeOrderStartup.CUSTOMER_NAME).orElseThrow();

            CoffeeOrder coffeeOrder = CoffeeOrder.builder().customer(customer).build();

            CoffeeOrderLine line = CoffeeOrderLine.builder()
                    .coffeeId(coffeeId)
                    .coffeeOrder(coffeeOrder)
                    .orderQuantity(new Random().nextInt(5) + 1) //zero based
                    .build();

            Set<CoffeeOrderLine> lines = new HashSet<>(1);
            lines.add(line);

            coffeeOrder.setCoffeeOrderLines(lines);

            coffeeOrderManager.newCoffeeOrder(coffeeOrder);
        });
    }

    private Optional<UUID> getRandomCoffee(){

        Optional<CoffeePagedList> listOptional = coffeeService.getListofCoffees();

        if (listOptional.isPresent()) {
            CoffeePagedList coffeePagedList = listOptional.get();

            if (coffeePagedList.getContent() != null && coffeePagedList.getContent().size() > 0) {
                List<CoffeeDto> dtoList = coffeePagedList.getContent();

                int k = new Random().nextInt(dtoList.size());

                return Optional.of(dtoList.get(k).getId());
            }
        }

        log.debug("Failed to get list of coffees");

        return Optional.empty();

    }
}