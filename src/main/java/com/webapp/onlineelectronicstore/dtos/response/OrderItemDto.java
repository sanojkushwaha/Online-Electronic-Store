package com.webapp.onlineelectronicstore.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private String orderItemId;

    private String productTitle;

    private String productImage;

    private int quantity;

    private double productPrice;

    private double totalPrice;
}
