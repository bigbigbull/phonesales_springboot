package com.lck.dao;

import com.lck.pojo.Category;
import com.lck.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/12/05
 */
public interface ProductDao extends JpaRepository<Product,Integer>{
   /**
    * 根据产品类型分页查询产品
    * @param category
    * @param pageable
    * @return Page
    */
   Page<Product> findByCategory(Category category, Pageable pageable);

   /**
    * 根据产品类型查询产品
    * @param category
    * @return List
    */
   List<Product> findByCategoryOrderById(Category category);

}
