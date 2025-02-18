package com.icia.rmate.controller;

import com.icia.rmate.dto.OrderDTO;
import com.icia.rmate.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpSession session; // Added autowiring for HttpSession

    @PostMapping(value = "/orderSave", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> saveOrder(@ModelAttribute OrderDTO orderDTO) {

        System.out.println(orderDTO + "orderDTO확인!");
        orderService.saveOrder(orderDTO);

        String script = "<script>"
                + "  window.opener.postMessage('paymentComplete', '*');"
                + "  window.close();"
                + "</script>";

        return ResponseEntity.status(HttpStatus.OK).body(script);
    }

    @GetMapping("/myOrdersList")
    public ResponseEntity<List<OrderDTO>> myOrdersList() {
        String loginId = (String) session.getAttribute("loginId"); // 세션에서 loginId 가져오기
        if (loginId == null || loginId.isEmpty()) { // 로그인 상태가 아니면 401 Unauthorized 상태 코드 반환
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<OrderDTO> myOrders = orderService.findMyOrders(loginId);
        System.out.println(myOrders + ": dto로 변환된 myOrders를 확인하라!");

        if (myOrders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(myOrders, HttpStatus.OK);
        }
    }
}