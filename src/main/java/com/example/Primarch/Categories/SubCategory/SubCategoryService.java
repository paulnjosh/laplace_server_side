package com.example.Primarch.Categories.SubCategory;

import com.example.Primarch.Categories.Category;
import com.example.Primarch.Categories.CategoryRepo;
import com.example.Primarch.Utils.EntityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubCategoryService {
    private final SubCategoryRepo subCategoryRepo;
    private final CategoryRepo categoryRepo;


    public EntityResponse addSubCategory(Long categoryId,SubCategory subCategory){
        EntityResponse res= new EntityResponse<>();
        try {
            Optional<Category> category= categoryRepo.findById(categoryId);
            if(!category.isPresent()){
                res.setMessage("Category not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                return res;
            }

            Optional<SubCategory> subCategory1=subCategoryRepo.findBySubCategoryName(subCategory.getSubCategoryName());
            if(subCategory1.isPresent()){
                res.setMessage("Sub category with  name "+subCategory.getSubCategoryName()+" already exists");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                return res;
            }
            subCategory.setCategory(category.get());
            subCategory=subCategoryRepo.save(subCategory);

            res.setMessage(HttpStatus.OK.getReasonPhrase());
            res.setStatusCode(HttpStatus.OK.value());
            res.setEntity(subCategory);

            res.setEntity(category);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }

    public EntityResponse updateSubCategory(SubCategory subCategory){
        EntityResponse res= new EntityResponse<>();
        try {
            Optional<SubCategory> subCategoryOptional=subCategoryRepo.findById(subCategory.getId());

            if(!subCategoryOptional.isPresent()){
                res.setMessage("Sub Category does not exist");
                res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                return res;
            }

            SubCategory presentSubCategory=subCategoryOptional.get();
            presentSubCategory.setSubCategoryName(subCategory.getSubCategoryName());
            presentSubCategory.setDescription(subCategory.getDescription());
            presentSubCategory.setStatus(subCategory.getStatus());

            presentSubCategory=subCategoryRepo.save(presentSubCategory);
            res.setMessage("Sub Category updated successfully");
            res.setStatusCode(HttpStatus.OK.value());
            res.setEntity(presentSubCategory);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return res;
    }
    public EntityResponse getAllSubCategories(){
        EntityResponse res= new EntityResponse<>();
        try {
            List<SubCategory> subCategories=subCategoryRepo.findAll();
            if(subCategories.size()<1){
                res.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
            res.setMessage(HttpStatus.FOUND.getReasonPhrase());
            res.setStatusCode(HttpStatus.FOUND.value());
            res.setEntity(subCategories);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }


}
