package com.example.demo.service;

import com.example.demo.dto.request.ProductRequestDto;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.module.Product;
import com.example.demo.module.ProductImages;
import com.example.demo.module.User;
import com.example.demo.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final UserService userService;
    private final S3Service s3Service;

    public void addProduct(ProductRequestDto productDto, Long userId, List<MultipartFile> images) {
        Product product = productMapper.toProduct(productDto);
        User user = userService.findById(userId);
        List<ProductImages> productImages = new ArrayList<>();
        for (MultipartFile image : images){
            try {
                var profileImage = UUID.randomUUID().toString();
                String imageUrl = "profile/product/%s/%s".formatted(userId, profileImage);
                s3Service.putObject(
                        "java-s3-bucket-test",
                        imageUrl,
                        image.getBytes()
                );
                ProductImages productImage = ProductImages.builder()
                        .imageUrl(imageUrl)
                        .product(product)
                        .build();
                productImages.add(productImage);
            } catch (IOException e) {
                throw new IllegalStateException("Fotoğraf yükleme hatası: " + e.getMessage());
            }
        }
        product.setImages(productImages);
        product.setUser(user);
        productRepo.save(product);
    }


    public List<Product> getProductByUserId(Long userId) {
        return productRepo.findByUserId(userId);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepo.findById(id).orElse(null);
        return productMapper.toResponseProduct(product);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    public void updateProduct(Long id, ProductRequestDto productDto) {
        Product product = productRepo.findById(id).orElseThrow(() -> new IllegalStateException("Ürün bulunamadı"));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        productRepo.save(product);
    }

    public void addProductImage(Long productId, MultipartFile image) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalStateException("Ürün bulunamadı"));
        try {
            var productImageRand = UUID.randomUUID().toString();
            String imageUrl = "profile/product/%s/%s".formatted(productId, productImageRand);
            s3Service.putObject(
                    "java-s3-bucket-test",
                    imageUrl,
                    image.getBytes()
            );
            ProductImages productImage = ProductImages.builder()
                    .imageUrl(imageUrl)
                    .product(product)
                    .build();
            product.getImages().add(productImage);
            productRepo.save(product);
        } catch (IOException e) {
            throw new IllegalStateException("Fotoğraf yükleme hatası: " + e.getMessage());
        }
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepo.findAll();

        return products.stream()
                .map(productMapper::toResponseProduct)
                .toList();
    }

    public void deleteProductImage(Long productId, Long imageId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalStateException("Ürün bulunamadı"));
        product.getImages().removeIf(productImages -> productImages.getId().equals(imageId));
        productRepo.save(product);
    }

    public byte[] getProductImage(Long productId, Long imageId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalStateException("Ürün bulunamadı"));
        ProductImages productImage = product.getImages().stream()
                .filter(productImages -> productImages.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Fotoğraf bulunamadı"));
        return s3Service.getObject("java-s3-bucket-test", productImage.getImageUrl());
    }




}
