package com.example.demo.repository;

import com.example.demo.dto.response.ProductResponse;
import com.example.demo.module.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM products p WHERE p.user.id = :userId")
    List<Product> findByUserId(Long userId);
}
