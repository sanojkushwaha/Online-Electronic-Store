package com.webapp.onlineelectronicstore.repositories;

import com.webapp.onlineelectronicstore.entites.Category;
import com.webapp.onlineelectronicstore.entites.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

    // Find products by category
    Page<Product> findByCategory(Category category, Pageable pageable);

    // Search products by title
    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Find products by live status
    Page<Product> findByLive(boolean live, Pageable pageable);

    // Find products by stock status
    Page<Product> findByStock(boolean stock, Pageable pageable);

    // Find products by brand
    Page<Product> findByBrandContainingIgnoreCase(String brand, Pageable pageable);

    // Search by title or description
    Page<Product> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title,
            String description,
            Pageable pageable
    );

    // Find products within a price range
    Page<Product> findByPriceBetween(
            double minPrice,
            double maxPrice,
            Pageable pageable
    );

    // Find products by category and live status
    Page<Product> findByCategoryAndLive(
            Category category,
            boolean live,
            Pageable pageable
    );

    // Find products by category and stock status
    Page<Product> findByCategoryAndStock(
            Category category,
            boolean stock,
            Pageable pageable
    );

    // Find products by brand and category
    Page<Product> findByBrandContainingIgnoreCaseAndCategory(
            String brand,
            Category category,
            Pageable pageable
    );

}