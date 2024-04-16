package com.example.demo.service;

import com.example.demo.dto.response.ProductResponse;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.module.Product;
import com.example.demo.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    public void addProduct(Product product) {
        productRepo.save(product);
    }


    public List<ProductResponse> getProductByUserId( Long userId) {
        return productRepo.findByUserId(userId);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepo.findById(id).orElse(null);
        return  productMapper.toResponseProduct(product);
    }


}
