package com.example.demo.repository;

import com.example.demo.dto.response.ProductResponse;
import com.example.demo.module.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<ProductResponse> findByUserId(Long userId);
}
