package com.hemant.productcategoryservice.repositories;

import com.hemant.productcategoryservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitleContaining(String query);

    Page<Product> findAllByTitleContainingAndCategory_Id(String query, Long categoryId, Pageable pageable);
}
