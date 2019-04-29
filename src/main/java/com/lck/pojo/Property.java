package com.lck.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/11/29
 */
@Entity
@Table(name="property")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name="cid")
    private Category category;

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
