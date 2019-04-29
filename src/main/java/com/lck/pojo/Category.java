package com.lck.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;


@SuppressWarnings({"ALL", "AlibabaClassMustHaveAuthor"})
@Entity
@Table(name = "category")
/** 使jpa不创建这两个无需json化的对象*/
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/11/29
 */
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private String name;

    @Transient
    private List<Product> products;

    @Transient
    private List<List<Product>> productsByRow;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<List<Product>> getProductsByRow() {
        return productsByRow;
    }

    public void setProductsByRow(List<List<Product>> productsByRow) {
        this.productsByRow = productsByRow;
    }
}
