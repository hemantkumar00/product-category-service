package com.hemant.productcategoryservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name="categories")
public class Category extends BaseModel{
    @Column(unique = true, nullable = false)
    private String title;
    @OneToMany(mappedBy = "category", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;
}
