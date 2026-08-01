package com.example.fullstackapp.service;

import com.example.fullstackapp.dto.ProductDto;
import com.example.fullstackapp.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product create(ProductDto dto);
    Product update(Long id, ProductDto dto);
    void deleteById(Long id);
    List<Product> search(String keyword);
}
