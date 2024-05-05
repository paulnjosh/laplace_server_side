package com.example.Primarch.inventory;

import com.example.Primarch.Categories.Category;
import com.example.Primarch.Utils.EntityResponse;
import com.example.Primarch.Utils.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockItemController {

    private final StockItemService stockItemService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam Long prodId,@RequestParam  Integer qty,@RequestParam  Double cost, @RequestParam String stockFor) {
        EntityResponse res= new EntityResponse<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd");
            Date parseDate = format.parse(stockFor);
            res=stockItemService.addStockItem(prodId,qty,cost,parseDate);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateStock(@RequestParam Long stockItemId,@RequestParam  Integer qty,@RequestParam  Double cost,@RequestParam String stockFor) {
        EntityResponse res= new EntityResponse<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd");
            Date parseDate = format.parse(stockFor);
            res=stockItemService.updateStockItem(stockItemId,qty,cost,parseDate);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all/by/date")
    public ResponseEntity<?> getStockItemsByDate(@RequestParam int page, @RequestParam int size, @RequestParam String date) {
        PaginationResponse res= new PaginationResponse();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd");
            Date parseDate = format.parse(date);
            res=stockItemService.getStockItemsByDate(page,size,parseDate);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
