package com.example.Primarch.Categories;

import com.example.Primarch.Utils.EntityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public EntityResponse addCategory(Category category){
        EntityResponse res= new EntityResponse<>();
        try {
            if(categoryRepo.existsByCategoryName(category.getCategoryName())){
                res.setMessage("Category with category name "+category.getCategoryName() +" already exists");
                res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());

                return res;
            }
            category=categoryRepo.save(category);
            res.setMessage(HttpStatus.CREATED.getReasonPhrase());
            res.setStatusCode(HttpStatus.CREATED.value());
            res.setEntity(category);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return res;
    }

    public EntityResponse updateCategory(Category category){
        EntityResponse res= new EntityResponse<>();
        try {
            Optional<Category> categoryOptional=categoryRepo.findById(category.getId());
            if(!categoryOptional.isPresent()){
                res.setMessage("Category does not exist");
                res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                return res;
            }

            category=categoryRepo.save(category);
            res.setMessage("Category updated successfully");
            res.setStatusCode(HttpStatus.OK.value());
            res.setEntity(category);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return res;
    }

    public EntityResponse getAllCategories(){
        EntityResponse res= new EntityResponse<>();
        try {
            List<Category> categories=categoryRepo.findAll();
            if(categories.size()<1){
                res.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
            res.setMessage(HttpStatus.FOUND.getReasonPhrase());
            res.setStatusCode(HttpStatus.FOUND.value());
            res.setEntity(categories);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return res;
    }

    public EntityResponse searchAll(String searchString){
        EntityResponse res= new EntityResponse<>();
        try {
            List<Category> categories=categoryRepo.searchAll(searchString);
            res.setMessage(HttpStatus.FOUND.getReasonPhrase());
            res.setStatusCode(HttpStatus.FOUND.value());
            res.setEntity(categories);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return res;
    }
}
