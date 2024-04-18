package com.example.demo.controller;

import com.example.demo.dto.request.ProductRequestDto;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.module.Product;
import com.example.demo.repository.ProductRepo;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/use/")
public class ProductController {

    private final ProductService productService;


    @PostMapping(value = "private/product/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addProduct(
            ProductRequestDto product, @PathVariable("userId") Long userId, List<MultipartFile> images) {

        productService.addProduct(product, userId, images);

    }

    @GetMapping("private/product/{userId}/user")
    public List<Product> getProductsByUserId(@PathVariable("userId") Long userId) {
        return productService.getProductByUserId(userId);
    }

    @GetMapping("public/product/{id}")
    public ProductResponse getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("private/product/{id}")
    public void updateProduct(@PathVariable("id") Long id, ProductRequestDto product) {
        productService.updateProduct(id, product);
    }

    @DeleteMapping("private/product/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }

    @PostMapping(value = "private/product/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addProductImage(@PathVariable("id") Long id, MultipartFile image) {
        productService.addProductImage(id, image);
    }

    @DeleteMapping("private/product/{id}/image/{imageId}")
    public void deleteProductImage(@PathVariable("id") Long id, @PathVariable("imageId") Long imageId) {
        productService.deleteProductImage(id, imageId);
    }

    @GetMapping(value = "public/product/{id}/image/{imageId}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getProductImage(@PathVariable("id") Long id, @PathVariable("imageId") Long imageId) {
        return productService.getProductImage(id, imageId);

    }

}
