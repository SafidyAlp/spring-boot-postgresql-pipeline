package com.example.crud.controller;

import com.example.crud.model.Product;
import com.example.crud.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ProductService productService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testGetAllProducts() throws Exception {
        Product product1 = new Product("Laptop", "High performance laptop", 999.99);
        Product product2 = new Product("Smartphone", "Latest smartphone", 699.99);
        
        Mockito.when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));
        
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Laptop")))
                .andExpect(jsonPath("$[1].name", is("Smartphone")));
    }
    
    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product("Laptop", "High performance laptop", 999.99);
        product.setId(1L);
        
        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.price", is(999.99)));
    }
    
    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product("Tablet", "Portable tablet", 299.99);
        Product savedProduct = new Product("Tablet", "Portable tablet", 299.99);
        savedProduct.setId(1L);
        
        Mockito.when(productService.createProduct(Mockito.any(Product.class))).thenReturn(savedProduct);
        
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Tablet")));
    }
}