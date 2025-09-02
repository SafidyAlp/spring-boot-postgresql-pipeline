package com.example.crud.controller;

import com.example.crud.model.Product;
import com.example.crud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    // PAGE D'ACCUEIL - Liste des produits
    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("product", new Product()); // Pour le formulaire
        return "products";
    }
    
    // CREATE (POST)
    @PostMapping
    public String createProduct(@ModelAttribute Product product) {
        productService.createProduct(product);
        return "redirect:/products";
    }
    
    // UPDATE (GET - Formulaire d'édition)
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        product.ifPresent(value -> model.addAttribute("product", value));
        return "edit-product";
    }
    
    // UPDATE (POST)
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.updateProduct(id, product);
        return "redirect:/products";
    }
    
    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
    
    // SEARCH
    @GetMapping("/search")
    public String searchProducts(@RequestParam String name, Model model) {
        List<Product> products = productService.searchProducts(name);
        model.addAttribute("products", products);
        model.addAttribute("searchTerm", name);
        return "products";
    }
}