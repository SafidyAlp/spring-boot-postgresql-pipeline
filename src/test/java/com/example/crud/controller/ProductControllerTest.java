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
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product("Laptop", "Gaming laptop", 1200.0);
        product1.setId(1L);

        product2 = new Product("Phone", "Smartphone", 800.0);
        product2.setId(2L);
    }

    @Test
    void testGetAllProducts() throws Exception {
        Mockito.when(productService.getAllProducts())
                .thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testCreateProduct() throws Exception {
        Mockito.when(productService.createProduct(any(Product.class))).thenReturn(product1);

        mockMvc.perform(post("/products")
                        .param("name", "Laptop")
                        .param("description", "Gaming laptop")
                        .param("price", "1200.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    void testEditProductForm() throws Exception {
        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(product1));

        mockMvc.perform(get("/products/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-product"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Mockito.when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(product1);

        mockMvc.perform(post("/products/update/1")
                        .param("name", "Updated Laptop")
                        .param("description", "New version")
                        .param("price", "1500.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(get("/products/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    void testSearchProducts() throws Exception {
        Mockito.when(productService.searchProducts("Laptop"))
                .thenReturn(Arrays.asList(product1));

        mockMvc.perform(get("/products/search")
                        .param("name", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("searchTerm", "Laptop"));
    }
}
