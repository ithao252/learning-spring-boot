package com.example.learnspringboot.controllers;
import com.example.learnspringboot.models.ProductModel;
import com.example.learnspringboot.models.ResponseObject;
import com.example.learnspringboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {
    //DI: tự động tạo ra 1 repository khi khởi động app
    @Autowired
    private ProductRepository productRepository;

    //this request is:GET http://localhost:8080/api/v1/Products/getAllProducts
    @GetMapping("/getAllProducts")
    List<ProductModel> getAllProducts(){
        return productRepository.findAll();
    }
    //this request is: http://localhost:8080/api/v1/Products/{id}
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        // let's return an object that's include: data, message, status,...
        Optional<ProductModel> foundProduct= productRepository.findById(id);
        return foundProduct.isPresent()?
          ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("200", "Query product successfully", foundProduct)
            ):
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("404", "Can not find product with id = "+ id, "null"));
    }

    //insert new Product with POST method
    //Postman : Raw, JSON
    //this request is:POST http://localhost:8080/api/v1/Products/insert
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody ProductModel newProduct){
        //different products must not have same name
        List<ProductModel> foundProducts= productRepository.findByProductName(newProduct.getProductName().trim());
        return foundProducts.size()>0?
             ResponseEntity.status((HttpStatus.NOT_IMPLEMENTED)).body(
              new ResponseObject("501", "Product name already taken", "null")
            ):
                ResponseEntity.status((HttpStatus.OK)).body(
                        new ResponseObject("200", "Insert product successfully", productRepository.save(newProduct)));
    }

    //update, upsert = update if found, otherwise insert
    //this request is:PUT http://localhost:8080/api/v1/Products/{id}
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody ProductModel newProduct, @PathVariable Long id){
        ProductModel updatedProduct= productRepository.findById(id)
                .map(product->{
                    product.setProductName(newProduct.getProductName());
                    product.setProductPrice(newProduct.getProductPrice());
                    product.setProductYear(newProduct.getProductYear());
                    return productRepository.save(product);
                }).orElseGet(()->{
                    newProduct.setId(id);
                    return productRepository.save(newProduct);}
                        );


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("200","Updated product successfully", updatedProduct)
        );
    }

    //Delete a Product => DELETE method
    //this request is:DELETE http://localhost:8080/api/v1/Products/{id}
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists= productRepository.existsById(id);
        if(exists) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("200", "Deleted product successfully", null)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("400", "Cannot find product to delete", null)
            );

    }
}
