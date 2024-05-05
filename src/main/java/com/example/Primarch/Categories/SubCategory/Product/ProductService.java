package com.example.Primarch.Categories.SubCategory.Product;
import com.example.Primarch.Categories.SubCategory.Product.ProductImage.ProductImage;
import com.example.Primarch.Categories.SubCategory.Product.ProductImage.ProductImageRepo;
import com.example.Primarch.Categories.SubCategory.SubCategory;
import com.example.Primarch.Categories.SubCategory.SubCategoryRepo;
import com.example.Primarch.Utils.EntityResponse;
import com.example.Primarch.Utils.PaginationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final ProductImageRepo productImageRepo;

    @Value("${images.directory}")
    String filePath;

    public EntityResponse addProduct(Long subCatId, Product product){
        EntityResponse res= new EntityResponse<>();
        try {
            Optional<SubCategory> subCategory= subCategoryRepo.findById(subCatId);
            if(!subCategory.isPresent()){
                res.setMessage("Sub Category not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                return res;
            }
            product.setSubCategory(subCategory.get());
            product.setCreatedOn(new Date());

            product=productRepo.save(product);

            res.setMessage(HttpStatus.OK.getReasonPhrase());
            res.setStatusCode(HttpStatus.OK.value());
            res.setEntity(product);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }

    public EntityResponse addProduct1(Long subCatId, Product product, MultipartFile file){
        EntityResponse res= new EntityResponse<>();
        try {
            Optional<SubCategory> subCategory= subCategoryRepo.findById(subCatId);
            if(!subCategory.isPresent()){
                res.setMessage("Sub Category not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                return res;
            }
            product.setSubCategory(subCategory.get());

//            String filePath="C:/projects/Files/uploaded/";
            System.out.println("About to process image");

            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath+file.getOriginalFilename());
            Files.write(path, bytes);

            ProductImage fileData=ProductImage.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .filePath(filePath)
                    .createdOn(new Date())
                    .build();

            System.out.println("File name "+file.getOriginalFilename());
            System.out.println("Content type "+file.getContentType());

            product.setProductImage(fileData);

            System.out.println("About to save product");
            product.setCreatedOn(new Date());
            product=productRepo.save(product);

            res.setMessage(HttpStatus.CREATED.getReasonPhrase());
            res.setStatusCode(HttpStatus.CREATED.value());
            res.setEntity(product);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }

    public EntityResponse updateProduct(Long subCatId, Product product, MultipartFile file){
        EntityResponse res= new EntityResponse<>();
        try {
            Optional<Product> product1=productRepo.findById(product.getId());
            if(!product1.isPresent()){
                res.setMessage("Prod not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                return res;
            }
            Product foundProd=product1.get();
            foundProd.setName(product.getName());
            foundProd.setDescription(product.getDescription());
            foundProd.setRetailPrice(product.getRetailPrice());
            foundProd.setWholesalePrice(product.getWholesalePrice());
            foundProd.setQty(product.getQty());
            foundProd.setStatus(product.getStatus());
            foundProd.setProductSize(product.getProductSize());
            foundProd.setColor(product.getColor());

            Optional<SubCategory> subCategory= subCategoryRepo.findById(subCatId);
            if(!subCategory.isPresent()){
                res.setMessage("Sub Category not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                return res;
            }
            foundProd.setSubCategory(subCategory.get());

//            String filePath="C:/projects/Files/uploaded/";
            System.out.println("About to process image");


            if(file!=null){

                ProductImage foundProdImage= foundProd.getProductImage();
                if(foundProdImage !=null){
                    foundProdImage.setProduct(null);
                    productImageRepo.save(foundProdImage);
                }
                byte[] bytes = file.getBytes();
                Path path = Paths.get(filePath+file.getOriginalFilename());
                Files.write(path, bytes);

                ProductImage fileData=ProductImage.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .filePath(filePath)
                        .createdOn(new Date())
                        .build();

                System.out.println("File name "+file.getOriginalFilename());
                System.out.println("Content type "+file.getContentType());

                foundProd.setProductImage(fileData);
            }


            System.out.println("About to save product");
//            foundProd.setCreatedOn(new Date());
            foundProd=productRepo.save(foundProd);

            res.setMessage("Product updated successfully");
            res.setStatusCode(HttpStatus.OK.value());
            res.setEntity(foundProd);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }

    public PaginationResponse getALlProducts(int page, int size){
        PaginationResponse res= new PaginationResponse();
        try {
            Pageable paging = PageRequest.of(page, size);

            Page<Product> products=productRepo.findAll(paging);

            if(products.getTotalElements()>0){
                List<Product> products1=products.getContent();

                res.setData(products1);
                res.setCurrentPage(page);
                res.setTotalPages(products.getTotalPages());
                res.setTotalItems(products.getTotalElements());
                res.setMessage(HttpStatus.FOUND.getReasonPhrase());
                res.setStatusCode(HttpStatus.FOUND.value());
            }else {
                res.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
            }

        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }

    public PaginationResponse searchProducts(int page, int size, String searchString){
        PaginationResponse res= new PaginationResponse();
        try {
            Pageable paging = PageRequest.of(page, size);

            Page<Product> products=productRepo.searchAll(paging,searchString);

            if(products.getTotalElements()>0){
                List<Product> products1=products.getContent();

                res.setData(products1);
                res.setCurrentPage(page);
                res.setTotalPages(products.getTotalPages());
                res.setTotalItems(products.getTotalElements());
                res.setMessage(HttpStatus.FOUND.getReasonPhrase());
                res.setStatusCode(HttpStatus.FOUND.value());
            }else {
                res.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
            }

        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }

    public EntityResponse deleteProduct(Long prodId){
        EntityResponse res=new EntityResponse<>();
        try {
            Optional<Product> product= productRepo.findById(prodId);
            if(!product.isPresent()){
                res.setMessage("Product not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
            productRepo.deleteById(prodId);

            res.setMessage("Product deleted successfully");
            res.setStatusCode(HttpStatus.OK.value());

        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Caught an error "+e);
        }
        return res;
    }
}
