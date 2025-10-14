package com.vamsi.MoneyManagerApp.controller;

import com.vamsi.MoneyManagerApp.dto.CategoryDTO;
import com.vamsi.MoneyManagerApp.entity.CategoryEntity;
import com.vamsi.MoneyManagerApp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("categories")
    public ResponseEntity<List<CategoryDTO>> getCategories(){
        return new ResponseEntity<>(categoryService.getCategoriesForCurrentUser(),HttpStatus.FOUND);
    }

    @GetMapping("categories/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByType(@PathVariable String type){
        return new ResponseEntity<>(categoryService.getCategoriesByTypeForCurrentUser(type),HttpStatus.FOUND);
    }

    @PostMapping("addCategory")
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody  CategoryDTO categoryDTO){

        CategoryDTO categoryDTO1 = categoryService.saveCategory(categoryDTO);

        return new ResponseEntity<>(categoryDTO1, HttpStatus.CREATED);
    }

    @PutMapping("category/update/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId ,@RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.updateCategory(categoryId,categoryDTO),HttpStatus.OK);
    }

}
