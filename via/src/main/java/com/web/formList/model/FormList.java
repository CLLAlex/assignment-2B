package com.web.formList.model;

import com.via.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class FormList {

    private String text;

    List<Product> productList = new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
