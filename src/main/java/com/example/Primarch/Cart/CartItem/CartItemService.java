package com.example.Primarch.Cart.CartItem;

import com.example.Primarch.Categories.SubCategory.Product.Product;
import com.example.Primarch.Categories.SubCategory.Product.ProductRepo;
import com.example.Primarch.UserProfile.UserProfile;
import com.example.Primarch.UserProfile.UserProfileRepo;
import com.example.Primarch.Utils.EntityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {
    private final CartItemRepo cartItemRepo;
    private final UserProfileRepo userProfileRepo;
    private final ProductRepo productRepo;

    public EntityResponse addCartItem(String userEmail, Long productId, Integer quantity){
        EntityResponse response= new EntityResponse<>();
        try {
            if(quantity<1){
                response.setMessage("Quantity should be greater than zero");
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                return response;
            }
            Optional<UserProfile> userProfile=userProfileRepo.findByEmail(userEmail);
            if(!userProfile.isPresent()){
                response.setMessage("User profile not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                return response;
            }

            Optional<Product> product=productRepo.findById(productId);
            if(!product.isPresent()){
                response.setMessage("Prod not found");
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                return response;
            }

            CartItem cartItem= new CartItem();
            cartItem.setProduct(product.get());
//            cartItem.setUserProfile(userProfile.get());
            cartItem.setQuantity(quantity);
            cartItem.setAddedOn(new Date());
            var cart= cartItemRepo.save(cartItem);
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setStatusCode(HttpStatus.OK.value());
            response.setEntity(cartItem);
        }catch (Exception e){
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getLocalizedMessage());
        }

        return response;
    }
}
