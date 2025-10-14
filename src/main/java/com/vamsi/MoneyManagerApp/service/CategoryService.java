package com.vamsi.MoneyManagerApp.service;

import com.vamsi.MoneyManagerApp.dto.CategoryDTO;
import com.vamsi.MoneyManagerApp.entity.CategoryEntity;
import com.vamsi.MoneyManagerApp.entity.ProfileEntity;
import com.vamsi.MoneyManagerApp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;


    public CategoryDTO saveCategory(CategoryDTO categoryDTO){

        ProfileEntity profile = profileService.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Category with this name already exists");
        }
         CategoryEntity newCategory = toEntity(categoryDTO,profile);
        categoryRepository.save(newCategory);
        return toDTO(newCategory);
    }

    public List<CategoryDTO> getCategoriesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> category = categoryRepository.findByProfileId(profile.getId());
        return category.stream().map(this::toDTO).toList();
    }

    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categoryEntities = categoryRepository.findByTypeAndProfileId(type,profile.getId());
        return categoryEntities.stream().map(this::toDTO).toList();
    }

    //helper method
    public CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile){
        return CategoryEntity.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .profile(profile)
                .icon(categoryDTO.getIcon())
                .type(categoryDTO.getType())
                .build();
    }

    //helper method
    public CategoryDTO toDTO(CategoryEntity categoryEntity){
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .type(categoryEntity.getType())
                .profile_id(categoryEntity.getProfile()!=null ? categoryEntity.getProfile().getId() : null)
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .build();
    }


    public CategoryDTO updateCategory(Long id,CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity entity = categoryRepository.findByIdAndProfileId(id, profile.getId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found or not accessible"));
        entity.setName(categoryDTO.getName());
        entity.setIcon(categoryDTO.getIcon());
        entity.setType(categoryDTO.getType());
        categoryRepository.save(entity);
        return toDTO(entity);
    }
}
