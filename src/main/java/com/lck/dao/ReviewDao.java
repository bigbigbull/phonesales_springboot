package com.lck.dao;

import com.lck.pojo.Product;
import com.lck.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/27
 */
public interface ReviewDao extends JpaRepository<Review, Integer> {

    List<Review> findByProductOrderByIdDesc(Product product);

    int countByProduct(Product product);

}
