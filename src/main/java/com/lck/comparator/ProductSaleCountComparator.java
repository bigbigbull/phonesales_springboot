package com.lck.comparator;

import com.lck.pojo.Product;

import java.util.Comparator;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/28
 */
public class ProductSaleCountComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount()-p1.getSaleCount();
    }

}
