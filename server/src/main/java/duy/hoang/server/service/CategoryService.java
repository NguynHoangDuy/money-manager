package duy.hoang.server.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import duy.hoang.server.dto.CategoryDTO;
import duy.hoang.server.entity.CategoryEntity;
import duy.hoang.server.entity.ProfileEntity;
import duy.hoang.server.repository.CategoryRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {
     private final ProfileService profileService;
     private final CategoryRepository categoryRepository;

     public CategoryDTO saveCategoryDTO(CategoryDTO categoryDTO) {
          ProfileEntity profile = profileService.getCurrentProfile();
          if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profile.getId())) {
               throw new RuntimeException("Category with this name already");
          }

          CategoryEntity newCategoryEntity = toEntity(categoryDTO, profile);
          newCategoryEntity = categoryRepository.save(newCategoryEntity);

          return toDTO(newCategoryEntity);
     }

     public List<CategoryDTO> getCategoriesForCurrentUser() {
          ProfileEntity profile = profileService.getCurrentProfile();
          List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
          return categories.stream().map(this::toDTO).toList();
     }

     public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type) {
          ProfileEntity profile = profileService.getCurrentProfile();
          List<CategoryEntity> categories = categoryRepository.findByTypeAndProfileId(type, profile.getId());
          return categories.stream().map(this::toDTO).toList();
     }

     public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
          ProfileEntity profile = profileService.getCurrentProfile();
          CategoryEntity updatedCategory = categoryRepository.findByIdAndProfileId(id, profile.getId()).orElseThrow(() -> new RuntimeException("Category not found or not accessible"));
          updatedCategory.setName(categoryDTO.getName());
          updatedCategory.setIcon(categoryDTO.getIcon());

          updatedCategory = categoryRepository.save(updatedCategory);
          return toDTO(updatedCategory);
     }

     private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile) {
          return CategoryEntity.builder()
                    .name(categoryDTO.getName())
                    .icon(categoryDTO.getIcon())
                    .profile(profile)
                    .type(categoryDTO.getType())
                    .build();
     }

     private CategoryDTO toDTO(CategoryEntity categoryEntity) {
          return CategoryDTO.builder()
                    .id(categoryEntity.getId())
                    .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
                    .name(categoryEntity.getName())
                    .icon(categoryEntity.getIcon())
                    .type(categoryEntity.getType())
                    .createdAt(categoryEntity.getCreatedAt())
                    .updatedAt(categoryEntity.getUpdatedAt())
                    .build();
     }
}
