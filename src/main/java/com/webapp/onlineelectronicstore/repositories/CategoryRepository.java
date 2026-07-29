package com.webapp.onlineelectronicstore.repositories;

import com.webapp.onlineelectronicstore.entites.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {


}
