package com.example.demo.dto.response;

import com.example.demo.module.ProductImages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String name;
    private String description;
    private Double price;
    private List<ProductImages> imagesUrl;
}
