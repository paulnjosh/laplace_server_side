package com.example.Primarch.inventory;

import com.example.Primarch.Categories.SubCategory.Product.Product;
import com.example.Primarch.Categories.SubCategory.Product.ProductRepo;
import com.example.Primarch.Utils.EntityResponse;
import com.example.Primarch.Utils.PaginationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockItemService {
    private final StockItemRepo stockItemRepo;
    private final ProductRepo productRepo;

    public EntityResponse addStockItem(Long prodId, Integer qty, Double cost, Date stockFor){
        EntityResponse res= new EntityResponse<>();
        try {
            Optional<Product> product= productRepo.findById(prodId);
            if(!product.isPresent()){
                res.setMessage("Product is not maintained");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                return res;
            }
            StockItem stockItem= new StockItem();
            stockItem.setCost(cost);
            stockItem.setQuantity(qty);
            stockItem.setStockFor(stockFor);
            stockItem.setCreatedOn(new Date());
            stockItem.setProduct(product.get());

            stockItem=stockItemRepo.save(stockItem);

            //query to update stock levels in product

            productRepo.updateSTock(prodId,qty);

            res.setEntity(stockItem);
            res.setMessage(HttpStatus.CREATED.getReasonPhrase());
            res.setStatusCode(HttpStatus.CREATED.value());

            return res;

        }catch (Exception e){
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setMessage(e.getLocalizedMessage());
        }
        return res;
    }

    public EntityResponse updateStockItem(Long stockItemId, Integer qty, Double cost,Date stockFor){
        EntityResponse res= new EntityResponse<>();
        try{
            Optional<StockItem> stockItem1= stockItemRepo.findById(stockItemId);
            if(!stockItem1.isPresent()){
                res.setMessage("Stock product not found");
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
                return res;
            }

//            Optional<Product> product= productRepo.findById(prodId);
//            if(!product.isPresent()){
//                res.setMessage("Product is not maintained");
//                res.setStatusCode(HttpStatus.NOT_FOUND.value());
//                return res;
//            }

            StockItem presentStockItem= stockItem1.get();
            presentStockItem.setQuantity(qty);
            presentStockItem.setCost(cost);
            presentStockItem.setStockFor(stockFor);
            presentStockItem.setModifiedOn(new Date());

            presentStockItem=stockItemRepo.save(presentStockItem);
//            presentStockItem.setProduct(product.get());

            res.setEntity(presentStockItem);
            res.setMessage(HttpStatus.OK.getReasonPhrase());
            res.setStatusCode(HttpStatus.OK.value());

        }catch (Exception e){
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setMessage(e.getLocalizedMessage());
        }
        return res;
    }

    public PaginationResponse getStockItemsByDate(int page, int size,Date date){
        PaginationResponse res= new PaginationResponse();
        try {
            Pageable paging = PageRequest.of(page, size);

            Page<StockItem> stockItems=stockItemRepo.findByDate(paging,date);
            if(stockItems.getTotalElements()>0){
                List<StockItem> stockItemList=stockItems.getContent();

                res.setData(stockItemList);
                res.setCurrentPage(page);
                res.setTotalPages(stockItems.getTotalPages());
                res.setTotalItems(stockItems.getTotalElements());
                res.setMessage(HttpStatus.FOUND.getReasonPhrase());
                res.setStatusCode(HttpStatus.FOUND.value());
            }else {
                res.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                res.setStatusCode(HttpStatus.NOT_FOUND.value());
            }
        }catch (Exception e){
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setMessage(e.getLocalizedMessage());
        }
        return res;
    }
}
