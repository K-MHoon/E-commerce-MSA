package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;

import java.util.List;

public interface OrderService {
    ResponseOrder createOrder(String userId, RequestOrder requestOrder);
    List<ResponseOrder> getOrderByUserId(String userId);
    ResponseOrder getOrderByOrderId(String orderId);
}
