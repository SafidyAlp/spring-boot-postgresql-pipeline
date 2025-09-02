package com.example.crud.controller;

import com.example.crud.model.Product;
import com.example.crud.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product product1;

    @BeforeEach
    void setUp() {
        product1 = new Product("Laptop", "Dell XPS", 1200.0);
        product1.setId(1L);
    }

    @Test
    void testGetAllProducts() throws Exception {
        Mockito.when(productService.getAllProducts()).thenReturn(List.of(product1));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("product")); // important pour le form
    }

    @Test
    void testCreateProduct() throws Exception {
        mockMvc.perform(post("/products")
                        .param("name", "Phone")
                        .param("description", "iPhone")
                        .param("price", "999.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(get("/products/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }
}
