package duy.hoang.server.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duy.hoang.server.dto.CategoryDTO;
import duy.hoang.server.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

     private final CategoryService categoryService;

     @PostMapping
     public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO) {
          CategoryDTO saveCategory = categoryService.saveCategoryDTO(categoryDTO);
          return ResponseEntity.status(HttpStatus.CREATED).body(saveCategory);
     }

     @GetMapping
     public ResponseEntity<List<CategoryDTO>> getCategories() {
          List<CategoryDTO> categories = categoryService.getCategoriesForCurrentUser();
          return ResponseEntity.ok(categories);
     }

     @GetMapping("/{type}")
     public ResponseEntity<List<CategoryDTO>> getCategories(@PathVariable String type) {
          List<CategoryDTO> categories = categoryService.getCategoriesByTypeForCurrentUser(type);
          return ResponseEntity.ok(categories);
     }

     @PutMapping("/{id}")
     public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
          CategoryDTO updated = categoryService.updateCategory(id, categoryDTO);

          return ResponseEntity.ok(updated);
     }

}
