package com.example.Primarch.ProductUiClassification;
import com.example.Primarch.Categories.SubCategory.Product.Product;
import com.example.Primarch.Categories.SubCategory.Product.ProductRepo;
import com.example.Primarch.Utils.EntityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductUiClassificationService {
    private final ProductUiClassificationRepo repo;
    private final ProductRepo productRepo;


    public EntityResponse addUiCategory(ProductUiClassification classification){
        EntityResponse res= new EntityResponse<>();
        try {
            Optional<ProductUiClassification> classification1= repo.findByClassificationName(classification.getClassificationName());
            if(classification1.isPresent()){
                res.setMessage("Classification already exists");
                res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                return res;
            }
            classification=repo.save(classification);
            res.setMessage(HttpStatus.OK.getReasonPhrase());
            res.setStatusCode(HttpStatus.OK.value());
            res.setEntity(classification);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }

    public EntityResponse addProductUiClassification(Long prodId, Long classificationId){
        EntityResponse res= new EntityResponse<>();
        try {
            Optional<ProductUiClassification> classification1= repo.findById(classificationId);
            if(!classification1.isPresent()){
                res.setMessage("Classification not found");
                res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                return res;
            }
            Optional<Product> product= productRepo.findById(prodId);
            if(!product.isPresent()){
                res.setMessage("Product not found");
                res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                return res;
            }

            Product existingProd= product.get();
            existingProd.setProductUiClassification(classification1.get());
            existingProd=productRepo.save(existingProd);

            res.setMessage(HttpStatus.OK.getReasonPhrase());
            res.setStatusCode(HttpStatus.OK.value());
            res.setEntity(existingProd);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }

    public EntityResponse findProductsByUiClassification( Long classificationId){
        EntityResponse res= new EntityResponse<>();
        try {
            Optional<ProductUiClassification> classification1= repo.findById(classificationId);
            if(!classification1.isPresent()){
                res.setMessage("Classification not found");
                res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                return res;
            }

            List<Product> existingProd= productRepo.findByProductUiClassification(classification1.get());

            if(existingProd.size()<0){
                res.setMessage("Products not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                return res;
            }

            res.setMessage(HttpStatus.OK.getReasonPhrase());
            res.setStatusCode(HttpStatus.OK.value());
            res.setEntity(existingProd);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }
}
