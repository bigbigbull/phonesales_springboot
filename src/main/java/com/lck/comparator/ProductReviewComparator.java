package com.lck.comparator;

import com.lck.pojo.Product;

import java.util.Comparator;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/28
 */
public class ProductReviewComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount()-p1.getReviewCount();
    }

}
