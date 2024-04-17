package com.example.demo.mapper;

import com.example.demo.dto.request.ProductRequestDto;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.module.Product;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    ProductResponse toResponseProduct(Product product);

    Product toProduct(ProductRequestDto productRequestDto);
}
