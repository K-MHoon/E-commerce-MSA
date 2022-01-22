package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ModelMapper mapper;
    private final KafkaProducer kafkaProducer;

    @Override
    public ResponseOrder createOrder(String userId, RequestOrder requestOrder) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDetails = mapper.map(requestOrder, OrderDto.class);

        orderDetails.setUserId(userId);
        orderDetails.setOrderId(UUID.randomUUID().toString());
        orderDetails.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

        Order order = mapper.map(orderDetails, Order.class);
        orderRepository.save(order);

        kafkaProducer.send("example-catalog-topic", orderDetails);

        return mapper.map(order, ResponseOrder.class);
    }

    @Override
    public List<ResponseOrder> getOrderByUserId(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(v -> mapper.map(v, ResponseOrder.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseOrder getOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new EntityNotFoundException("해당 orderId를 찾지 못했습니다."));
        return mapper.map(order, ResponseOrder.class);
    }
}
