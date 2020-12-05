package com.sourabh.cafeorder.sm.actions;

import com.sourabh.cafeorder.config.JmsConfig;
import com.sourabh.cafeorder.domain.CoffeeOrder;
import com.sourabh.cafeorder.domain.CoffeeOrderEventEnum;
import com.sourabh.cafeorder.domain.CoffeeOrderStatusEnum;
import com.sourabh.cafeorder.mapper.CoffeeOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import static com.sourabh.cafeorder.service.CoffeeOrderManagerImpl.ORDER_OBJECT_HEADER;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCoffeeOrder implements Action<CoffeeOrderStatusEnum, CoffeeOrderEventEnum> {

    private final JmsTemplate jmsTemplate;
    private final CoffeeOrderMapper coffeeOrderMapper;

    @Override
    public void execute(StateContext<CoffeeOrderStatusEnum, CoffeeOrderEventEnum> stateContext) {
        CoffeeOrder coffeeOrder = stateContext.getStateMachine().getExtendedState()
                .get(ORDER_OBJECT_HEADER, CoffeeOrder.class);

        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_QUEUE, ValidateCoffeeOrderRequest
                .builder()
                .coffeeOrder(coffeeOrderMapper.coffeeOrderToDto(coffeeOrder))
                .build());

        log.debug("Sent request to queue" + JmsConfig.VALIDATE_ORDER_QUEUE + "for Coffee Order Id: " + coffeeOrder.getId().toString());
    }
}