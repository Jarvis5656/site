package com.Shopping.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Shopping.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
