package com.sourabh.cafeorder.sm;

import com.sourabh.cafeorder.domain.CoffeeOrder;
import com.sourabh.cafeorder.domain.CoffeeOrderEventEnum;
import com.sourabh.cafeorder.domain.CoffeeOrderStatusEnum;
import com.sourabh.cafeorder.mapper.DateMapper;
import com.sourabh.cafeorder.repository.CoffeeOrderRepository;
import com.sourabh.cafeorder.service.CoffeeOrderManagerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class CoffeeOrderStateChangeInterceptor extends StateMachineInterceptorAdapter<CoffeeOrderStatusEnum, CoffeeOrderEventEnum> {

    private final CoffeeOrderRepository coffeeOrderRepository;
    private final RestTemplateBuilder restTemplateBuilder;
    private final DateMapper dateMapper = new DateMapper();

    @Transactional
    @Override
    public void preStateChange(State<CoffeeOrderStatusEnum, CoffeeOrderEventEnum> state, Message<CoffeeOrderEventEnum> message, Transition<CoffeeOrderStatusEnum, CoffeeOrderEventEnum> transition, StateMachine<CoffeeOrderStatusEnum, CoffeeOrderEventEnum> stateMachine) {
        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(CoffeeOrderManagerImpl.ORDER_ID_HEADER, " ")))
                .ifPresent(orderId -> {
                    log.debug("Saving state for order id: " + orderId + " Status: " + state.getId());

                    CoffeeOrder coffeeOrder = coffeeOrderRepository.getOne(UUID.fromString(orderId));
                    coffeeOrder.setOrderStatus(state.getId());
                    coffeeOrderRepository.saveAndFlush(coffeeOrder);
                });
    }
}
