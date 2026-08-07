package com.webapp.onlineelectronicstore.entites;

import com.webapp.onlineelectronicstore.entites.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @UuidGenerator
    @Column(name = "product_id")
    private String productId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private double price;

    // Discount percentage
    private double discount;

    // Final price after discount
    private double discountedPrice;

    private int quantity;

    @Column(length = 1000)
    private String productImage;

    private String brand;

    private boolean live;

    private boolean stock;

    private Date addedDate;

    // Many products belong to one category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}