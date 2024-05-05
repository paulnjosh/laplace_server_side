package com.example.Primarch.Categories.SubCategory.Product;

import com.example.Primarch.Utils.EntityResponse;
import com.example.Primarch.Utils.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestParam Long subCatId, @RequestBody Product product) {
        EntityResponse res= new EntityResponse<>();
        try {
            res=productService.addProduct(subCatId,product);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

//    @PostMapping("/add1")
//    public ResponseEntity<?> addProduct1(@RequestParam("subCatId") Long subCatId, @ModelAttribute Product product,@RequestParam("image") MultipartFile file) {
//        EntityResponse res= new EntityResponse<>();
//        try {
//            res=productService.addProduct1(subCatId,product,file);
//        }catch (Exception e){
//            res.setMessage(e.getLocalizedMessage());
//            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//        return new ResponseEntity<>(res, HttpStatus.OK);
//    }

    @PostMapping("/add1")
    public ResponseEntity<?> addProduct1(@RequestParam Long subCatId,
                                         @RequestParam String name,
                                         @RequestParam String description,
                                         @RequestParam Double retailPrice,
                                         @RequestParam Double wholesalePrice,
                                         @RequestParam Integer qty,
                                         @RequestParam String status,
                                         @RequestParam String productSize,
                                         @RequestParam String color,
                                         @RequestParam(value = "image", required = false) MultipartFile file) {
        EntityResponse res= new EntityResponse<>();
        try {
            Product product= Product.builder()
                    .name(name)
                    .description(description)
                    .retailPrice(retailPrice)
                    .wholesalePrice(wholesalePrice)
                    .qty(qty)
                    .status(status)
                    .productSize(productSize)
                    .color(color)
            .build();
            System.out.println("About to call service");
            res=productService.addProduct1(subCatId,product,file);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProduct1(
            @RequestParam Long id,
            @RequestParam Long subCatId,
                                         @RequestParam String name,
                                         @RequestParam String description,
                                         @RequestParam Double retailPrice,
                                         @RequestParam Double wholesalePrice,
                                         @RequestParam Integer qty,
                                         @RequestParam String status,
                                         @RequestParam String productSize,
                                         @RequestParam String color,
                                         @RequestParam(value = "image", required = false) MultipartFile file) {
        EntityResponse res= new EntityResponse<>();
        try {
            Product product= Product.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .retailPrice(retailPrice)
                    .wholesalePrice(wholesalePrice)
                    .qty(qty)
                    .status(status)
                    .productSize(productSize)
                    .color(color)
                    .build();
            System.out.println("About to call service");
            res=productService.updateProduct(subCatId,product,file);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getALl(@RequestParam int page, @RequestParam int size) {
        PaginationResponse res= new PaginationResponse();
        try {
            res=productService.getALlProducts(page,size);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAll(@RequestParam int page, @RequestParam int size, @RequestParam String searchString) {
        PaginationResponse res= new PaginationResponse();
        try {
            res=productService.searchProducts(page,size,searchString);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long prodId) {
        EntityResponse res= new EntityResponse();
        try {
            res=productService.deleteProduct(prodId);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
