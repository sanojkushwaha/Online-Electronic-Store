package com.webapp.onlineelectronicstore.dtos.response;

import com.webapp.onlineelectronicstore.enums.OrderStatus;
import com.webapp.onlineelectronicstore.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private String orderId;

    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;

    private String billingName;

    private String billingPhone;

    private String billingAddress;

    private String paymentMethod;

    private double orderAmount;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private UserDto user;

    @Builder.Default
    private List<OrderItemDto> orderItems = new ArrayList<>();
}