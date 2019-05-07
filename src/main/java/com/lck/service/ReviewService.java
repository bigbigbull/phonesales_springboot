package com.lck.service;

import com.lck.dao.ReviewDao;
import com.lck.pojo.Product;
import com.lck.pojo.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/27
 */
@Service
@CacheConfig(cacheNames = "reviews")
public class ReviewService {
    @Autowired
    ReviewDao reviewDao;
    @Autowired
    ProductService productService;

    @CacheEvict(allEntries = true)
    public void add(Review review) {
        reviewDao.save(review);
    }

    @Cacheable(key="'reviews-pid-'+#p0.id")
    public List<Review> list(Product product){
        List<Review> result =  reviewDao.findByProductOrderByIdDesc(product);
        return result;
    }

    @Cacheable(key = "'reviews-count-pid-'+#p0.id")
    public int getCount(Product product) {
        return reviewDao.countByProduct(product);
    }
}
