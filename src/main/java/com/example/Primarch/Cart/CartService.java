package com.example.Primarch.Cart;

import com.example.Primarch.Cart.CartItem.CartItem;
import com.example.Primarch.Cart.Requests.AddCartReq;
import com.example.Primarch.Cart.Requests.ProdCartHolder;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepo cartRepo;
    private final ProductRepo productRepo;

    public EntityResponse addCart(AddCartReq req){
        EntityResponse res= new EntityResponse<>();
        try {
            //make sure there is atleast one product
            //do product verification
            res=verifyProducts(req.getProducts());
            if(res.getStatusCode()!=HttpStatus.OK.value()){
                return res;
            }

            List<CartItem> cartItems= (List<CartItem>) res.getEntity();
//            Double subTotals= cartItems.stream().mapToDouble(CartItem::getRetailPrice*CartItem::getQuantity)
//                    .sum();
            double subTotals = cartItems.stream()
                    .mapToDouble(item -> item.getRetailPrice() * item.getQuantity())
                    .sum();

            Double amtPaid=req.getAmtPaidCash()+req.getAmtPaidMpesa();
            Double discountGiven=req.getTotalDiscount();

            //check the balancing
            if(Math.abs(subTotals-(amtPaid+discountGiven))>1){
                System.out.println("Amount paid :: "+amtPaid);
                System.out.println("Discount given :: "+discountGiven);
                System.out.println("Sub totals :: "+subTotals);
                res.setMessage("The subtotals, amt paid and discount given does not balance");
                res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                return res;
            }
            //create and save cart

            Cart cart= new Cart();
            cart.setSubTotal(subTotals);
            cart.setDiscountTotal(discountGiven);
            cart.setAmtPaidMpesa(req.getAmtPaidMpesa());
            cart.setAmtPaidCash(req.getAmtPaidCash());
            cart.setCartItems(cartItems);
            cart.setCreatedOn(new Date());

            cart=cartRepo.save(cart);
            res.setEntity("Cart processed successfully");
            res.setStatusCode(HttpStatus.OK.value());
            res.setEntity(cart);
            return res;


        }catch (Exception e){
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setMessage(e.getLocalizedMessage());
        }
        return res;
    }

    private EntityResponse verifyProducts(List<ProdCartHolder> products){
        EntityResponse res= new EntityResponse<>();
        try {
            if(products==null){
                res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                res.setMessage("There should be at least one product in the cart");
                return res;
            }
            if(products.size()<1){
                res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                res.setMessage("There should be at least one product in the cart");
                return res;
            }

//            Double expectedTotals=0.0;
            List<CartItem> cartItems = new ArrayList<>();

            for(ProdCartHolder prod :products){
                System.out.println(prod);
                if(prod.getQuantity()<1){
                    res.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                    res.setMessage("ALl products quantities should be greater than zero");
                    return res;
                }
                Optional<Product> product=productRepo.findById(prod.getProdId());
                if(! product.isPresent()){
                    res.setStatusCode(HttpStatus.NOT_FOUND.value());
                    res.setMessage("Product with prod id "+prod.getProdId()+" not found");
                    return res;
                }
                //check if the prod is out of stock
                //calculate expected totals
//                expectedTotals=expectedTotals+(product.get().getRetailPrice()*prod.getQuantity());
                CartItem cartItem= new CartItem();
                cartItem.setProduct(product.get());
                cartItem.setQuantity(prod.getQuantity());
                cartItem.setAddedOn(new Date());
                cartItem.setRetailPrice(product.get().getRetailPrice());
                cartItem.setWholesalePrice(product.get().getWholesalePrice());

                cartItems.add(cartItem);
            }
            res.setStatusCode(HttpStatus.OK.value());
            res.setMessage(HttpStatus.OK.getReasonPhrase());
            res.setEntity(cartItems);
            return res;
        }catch (Exception e){
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setMessage(e.getLocalizedMessage());
        }
        return res;
    }


    public PaginationResponse getCartItems(int page, int size, Date date){
        PaginationResponse res= new PaginationResponse();
        try {
            Pageable paging = PageRequest.of(page, size);

            Page<Cart> cart=cartRepo.findByDate(paging,date);
            if(cart.getTotalElements()>0){
                List<Cart> carts=cart.getContent();

                res.setData(carts);
                res.setCurrentPage(page);
                res.setTotalPages(cart.getTotalPages());
                res.setTotalItems(cart.getTotalElements());
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
