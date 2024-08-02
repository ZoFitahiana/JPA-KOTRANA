package com.fresh.coding.attachapijavains3.mapper;

import com.fresh.coding.attachapijavains3.dto.CategorieForm;
import com.fresh.coding.attachapijavains3.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategorieMapper {

    public Category toReceiveDataForm(CategorieForm categorieForm){
        return  new Category(null,categorieForm.name(),categorieForm.description());
    }

    public  Category toEntity(CategorieForm  categorieForm, Long id){
        return  new Category(id.toString(), categorieForm.name(), categorieForm.description());
    }

}
