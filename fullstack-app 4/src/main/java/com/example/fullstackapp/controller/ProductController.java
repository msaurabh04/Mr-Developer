package com.example.fullstackapp.controller;

import com.example.fullstackapp.dto.ProductDto;
import com.example.fullstackapp.entity.Product;
import com.example.fullstackapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String listProducts(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("products", productService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "products/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("formTitle", "Add New Product");
        return "products/form";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute("product") ProductDto dto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "Add New Product");
            return "products/form";
        }
        productService.create(dto);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setCategory(product.getCategory());

        model.addAttribute("product", dto);
        model.addAttribute("formTitle", "Edit Product");
        return "products/form";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                 @Valid @ModelAttribute("product") ProductDto dto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "Edit Product");
            return "products/form";
        }
        productService.update(id, dto);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "products/view";
    }
}
