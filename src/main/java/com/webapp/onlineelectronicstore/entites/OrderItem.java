package com.webapp.onlineelectronicstore.entites;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @UuidGenerator
    @Column(name = "order_item_id")
    private String orderItemId;

    private int quantity;

    @Column(name = "product_price", nullable = false)
    private double productPrice;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "product_title", nullable = false)
    private String productTitle;

    @Column(name = "product_image")
    private String productImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}