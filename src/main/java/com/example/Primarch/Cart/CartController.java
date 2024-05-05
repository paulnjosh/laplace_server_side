package com.example.Primarch.Cart;

import com.example.Primarch.Cart.Requests.AddCartReq;
import com.example.Primarch.Utils.EntityResponse;
import com.example.Primarch.Utils.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody AddCartReq req) {
        EntityResponse res= new EntityResponse<>();
        try {
            res= cartService.addCart(req);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all/by/date")
    public ResponseEntity<?> getCartsByDate(@RequestParam int page, @RequestParam int size, @RequestParam String date) {
        PaginationResponse res= new PaginationResponse();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd");
            Date parseDate = format.parse(date);
            res=cartService.getCartItems(page,size,parseDate);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
