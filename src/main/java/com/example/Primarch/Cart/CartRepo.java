package com.example.Primarch.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    @Query(nativeQuery = true,value = "SELECT * FROM `cart` WHERE date(`created_on`)=date(:date)")
    Page<Cart> findByDate(Pageable paging, Date date);
}
