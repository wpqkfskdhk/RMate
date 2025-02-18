package com.icia.rmate.service;

import com.icia.rmate.dao.OrderRepository;
import com.icia.rmate.dto.OrderDTO;
import com.icia.rmate.dto.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void saveOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = OrderEntity.toEntity(orderDTO);
        System.out.println(orderEntity + "orderEntity확인!");
        orderRepository.save(orderEntity);
    }

    public List<OrderDTO> findMyOrders(String loginId) {
        System.out.println("userId : " + loginId); //Using the loginId parameter

        if (loginId == null) {
            System.out.println("userId가 null입니다.");
            return Collections.emptyList();
        }
        if (loginId.isEmpty()) {
            System.out.println("userId가 비어있습니다.");
            return Collections.emptyList();
        }
        try {
            List<OrderEntity> myOrders = orderRepository.findByUserId(loginId);
            if (myOrders.isEmpty()) {
                System.out.println("결제 내역이 없습니다.");
                return Collections.emptyList();
            }
            for (OrderEntity order : myOrders) {
                System.out.println("주문 고유 번호 : " + order.getOrder_Id() + ", 상품명 : " + order.getProduct_Name() + ", 총 금액: " + order.getAmount() + " 생성일시 : " + order.getCreated_At());
            }
            return myOrders.stream()
                    .map(OrderDTO::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}