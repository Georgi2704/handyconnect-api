package com.georgivasil.springjwt.controllers;


import com.georgivasil.springjwt.exceptions.NotFoundException;
import com.georgivasil.springjwt.models.Category;
import com.georgivasil.springjwt.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepo;

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createCategory(@RequestBody Category category){
        categoryRepo.save(category);
        return "Category created successfully: " + category.getTitle();
    }

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public Category editCategory(@RequestBody Category category, @PathVariable long id){
        Optional<Category> categoryOptional = categoryRepo.findById(id);

        if (!categoryOptional.isPresent()) {
            throw new NotFoundException("id-" + id);
        } else {
            Category newCategory = categoryOptional.get();
            newCategory.setTitle(category.getTitle());
            categoryRepo.save(newCategory);
            return newCategory;
        }
    }

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable long id) {
        Optional<Category> categoryOptional = categoryRepo.findById(id);
        if (!categoryOptional.isPresent()) {
            throw new NotFoundException("id-" + id);
        }
        categoryRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public List<Category> getCategories() {
        {
            return categoryRepo.findAll();
        }
    }

    @CrossOrigin
    @GetMapping("/find/{id}")
    public Optional<Category> findCategory(@PathVariable long id) {
        Optional<Category> categoryOptional = categoryRepo.findById(id);

        if (!categoryOptional.isPresent()) {
            throw new NotFoundException("id-" + id);
        }

        return categoryRepo.findById(id);
    }

}
