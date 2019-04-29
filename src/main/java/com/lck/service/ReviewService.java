package com.lck.service;

import com.lck.dao.ReviewDao;
import com.lck.pojo.Product;
import com.lck.pojo.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/27
 */
@Service
public class ReviewService {
    @Autowired
    ReviewDao reviewDao;
    @Autowired
    ProductService productService;

    public void add(Review review) {
        reviewDao.save(review);
    }

    public List<Review> list(Product product){
        List<Review> result =  reviewDao.findByProductOrderByIdDesc(product);
        return result;
    }

    public int getCount(Product product) {
        return reviewDao.countByProduct(product);
    }
}
