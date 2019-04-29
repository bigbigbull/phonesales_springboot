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
    /**
     * 根据产品查询评论信息
     * @param product
     * @return
     */
    List<Review> findByProductOrderByIdDesc(Product product);

    /**
     * 根据产品查询评论数量
     * @param product
     * @return
     */
    int countByProduct(Product product);

}
