package com.example.Primarch.Categories.SubCategory;
import com.example.Primarch.Categories.Category;
import com.example.Primarch.Utils.EntityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sub-cat")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam Long catId,@RequestBody SubCategory subCategory) {
        EntityResponse res= new EntityResponse<>();
        try {
            res=subCategoryService.addSubCategory(catId,subCategory);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getALl() {
        EntityResponse res= new EntityResponse<>();
        try {
            res=subCategoryService.getAllSubCategories();
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody SubCategory subCategory) {
        EntityResponse res= new EntityResponse<>();
        try {
            res=subCategoryService.updateSubCategory(subCategory);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
