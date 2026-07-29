package com.webapp.onlineelectronicstore.entites;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @UuidGenerator
    @Column(name = "cart_item_id")
    private String cartItemId;

    private int quantity;

    private double totalPrice;

    // Many CartItems -> One Cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // Many CartItems -> One Product
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
}