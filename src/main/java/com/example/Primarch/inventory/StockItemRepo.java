package com.example.Primarch.inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface StockItemRepo extends JpaRepository<StockItem, Long> {

    @Query(nativeQuery = true,value = "SELECT * FROM `stock_item` WHERE date(`stock_for`)=date(:date)")
    Page<StockItem> findByDate(Pageable paging, Date date);
}
