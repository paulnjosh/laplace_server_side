package com.example.Primarch.ProductUiClassification;

import com.example.Primarch.Categories.SubCategory.Product.Product;
import com.example.Primarch.Utils.EntityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prod-ui-classification")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProductUiClassificationController {
    private final ProductUiClassificationService service;

    @PostMapping("/add")
    public ResponseEntity<?> addUiCat(@RequestBody ProductUiClassification classification) {
        EntityResponse res= new EntityResponse<>();
        try {
            res=service.addUiCategory(classification);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PostMapping("/add/prod-ui-cat")
    public ResponseEntity<?> addUiCatProd(@RequestParam Long prodId,@RequestParam Long classificationId) {
        EntityResponse res= new EntityResponse<>();
        try {
            res=service.addProductUiClassification(prodId,classificationId);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getProductsByClassification(@RequestParam Long classificationId) {
        EntityResponse res= new EntityResponse<>();
        try {
            res=service.findProductsByUiClassification(classificationId);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
