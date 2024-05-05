package com.example.Primarch.Cart.CartItem;

import com.example.Primarch.Utils.EntityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart-item")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CartItemController {
    private final CartItemService cartItemService;
    @PostMapping("/add")
    public ResponseEntity<?> add(
            @RequestParam String userEmail,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        EntityResponse res= new EntityResponse<>();
        try {
            res= cartItemService.addCartItem(userEmail, productId, quantity);
        }catch (Exception e){
            res.setMessage(e.getLocalizedMessage());
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
