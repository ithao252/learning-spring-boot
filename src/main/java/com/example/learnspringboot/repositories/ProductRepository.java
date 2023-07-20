package com.example.learnspringboot.repositories;


import com.example.learnspringboot.models.ProductModel;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import java.util.List;


// nơi để lấy data về
@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByProductName(String productName);

}
