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
@RequiredArgsConstructor
@Component
public class AllocateCoffeeOrder implements Action<CoffeeOrderStatusEnum, CoffeeOrderEventEnum> {

    private final JmsTemplate jmsTemplate;
    private final CoffeeOrderMapper coffOrderMapper;


    @Override
    public void execute(StateContext<CoffeeOrderStatusEnum, CoffeeOrderEventEnum> context) {

        log.debug("Sending Allocation Request...");

        CoffeeOrder coffOrder = context.getStateMachine().getExtendedState()
                .get(ORDER_OBJECT_HEADER, CoffeeOrder.class);

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_QUEUE, AllocateCoffeeOrderRequest
                .builder()
                .coffOrder(coffOrderMapper.coffeeOrderToDto(coffOrder))
                .build());

        log.debug("Sent request to queue" + JmsConfig.ALLOCATE_ORDER_QUEUE + "for Coffee Order Id: " + coffOrder.getId().toString());
    }
}