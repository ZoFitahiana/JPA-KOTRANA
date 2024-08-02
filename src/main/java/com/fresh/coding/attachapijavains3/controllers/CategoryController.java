package com.fresh.coding.attachapijavains3.controllers;

import com.fresh.coding.attachapijavains3.entities.Category;
import com.fresh.coding.attachapijavains3.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        return categoryService.findAllCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Category> saveAllCategories(@RequestBody List<Category> categories) {
        return categoryService.saveAllCategories(categories);
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Category getCategoryById(@PathVariable("id") Long id) {
        return categoryService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
    }
}
