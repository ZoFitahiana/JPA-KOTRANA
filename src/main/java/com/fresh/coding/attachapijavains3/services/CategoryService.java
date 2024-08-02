package com.fresh.coding.attachapijavains3.services;


import com.fresh.coding.attachapijavains3.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAllCategories();

    void deleteById(Long id);

    Category findById(Long id);

    List<Category> saveAllCategories(List<Category> categories);
}
