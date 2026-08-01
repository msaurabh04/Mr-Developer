package com.example.fullstackapp.controller;

import com.example.fullstackapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping({"/", "/home"})
    public String home(Authentication authentication, Model model) {
        boolean loggedIn = authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());

        // Product catalog is public - visible to guests and logged-in users alike
        model.addAttribute("products", productService.findAll());

        if (loggedIn) {
            return "home";
        }
        return "index";
    }
}
