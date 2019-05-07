package com.lck.dao;

import com.lck.pojo.Product;
import com.lck.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/12/19
 */
public interface ProductImageDao extends JpaRepository<ProductImage,Integer> {
    /**
     * 根据产品和图片类型查询产品图片集合
     *
     * @param product
     * @param type
     * @return  List
     */
    List<ProductImage> findByProductAndTypeOrderById(Product product,String type);
    
}
