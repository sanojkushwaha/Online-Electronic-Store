package com.webapp.onlineelectronicstore.entites;

import com.webapp.onlineelectronicstore.enums.OrderStatus;
import com.webapp.onlineelectronicstore.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @UuidGenerator
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "billing_name", nullable = false)
    private String billingName;

    @Column(name = "billing_phone", nullable = false)
    private String billingPhone;

    @Column(name = "billing_address", nullable = false, length = 500)
    private String billingAddress;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "order_amount")
    private double orderAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    // Many Orders -> One User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // One Order -> Many OrderItems
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();
}