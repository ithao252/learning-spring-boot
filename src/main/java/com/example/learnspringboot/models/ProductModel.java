package com.example.learnspringboot.models;

import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Objects;

//POJO: plain object java object=> generate to json file
@Entity
@Table(name="tblProduct")
public class ProductModel {
    @Id
    @SequenceGenerator(
            //tên của generator
            name = "product_sequence_gen",
            // tên của sequence trong database
            sequenceName = "product_sequence_db",
            allocationSize = 1 //increment by 1
    )
    @GeneratedValue(
            //giá trị của id sẽ được tạo bởi sequence định nghĩa trước đó
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence_gen"
    )
    //this is primary key
    private Long id;
    //validate: constraint
    @Column(nullable = false, unique = true, length = 200)
    private String productName;
    private int productYear;
    private Double productPrice;
    private String url;

    public ProductModel() {
    }
    //calculated field = transient, not exist in MySql
    @Transient
    private int age;//age is calculated from "year"
    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - productYear;
    }
    public ProductModel( String productName, int productYear, Double productPrice, String url) {
        this.productName = productName;
        this.productYear = productYear;
        this.productPrice = productPrice;
        this.url = url;
    }

    //
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductYear() {
        return productYear;
    }

    public void setProductYear(int productYear) {
        this.productYear = productYear;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", year=" + productYear +
                ", productPrice=" + productPrice +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductModel that)) return false;
        return getProductYear() == that.getProductYear()
                && getAge() == that.getAge()
                && Objects.equals(getId(), that.getId())
                && Objects.equals(getProductName(), that.getProductName())
                && Objects.equals(getProductPrice(), that.getProductPrice())
                && Objects.equals(getUrl(), that.getUrl());
    }


}
