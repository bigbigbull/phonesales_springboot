package com.lck.service;

import com.lck.dao.ProductImageDao;
import com.lck.enums.ProductImageTypeEnum;
import com.lck.pojo.OrderItem;
import com.lck.pojo.Product;
import com.lck.pojo.ProductImage;
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
 * @date 2018/12/20
 */
@Service
@CacheConfig(cacheNames = "productImages")
public class ProductImageService {
    @Autowired
    ProductImageDao productImageDao;
    @Autowired
    ProductService productService;

    @CacheEvict(allEntries = true)
    public void add(ProductImage productImage) {
        productImageDao.save(productImage);
    }

    @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        productImageDao.delete(id);
    }

    @Cacheable(key="'productImages-one-'+#p0")
    public ProductImage get(Integer id) {
        return productImageDao.findOne(id);
    }

    @Cacheable(key="'productImages-single-pid-'+#p0.id")
    public List<ProductImage> listSingleProductImages(Product product) {
        return productImageDao.findByProductAndTypeOrderById(product, ProductImageTypeEnum.TYPE_SINGLE.toString());
    }

    @Cacheable(key = "'productImages-detail-pid-'+#p0.id")
    public List<ProductImage> listDetailProductImages(Product product) {
        return productImageDao.findByProductAndTypeOrderById(product, ProductImageTypeEnum.TYPE_DETAIL.toString());
    }

    public void setFirstProductImage(Product product) {
        List<ProductImage> singleImages = listSingleProductImages(product);
        if (!singleImages.isEmpty()) {
            product.setFirstProductImage(singleImages.get(0));
        } else {
            // 这样做是考虑到产品还没有来得及设置图片，但是在订单后台管理里查看订单项的对应产品图片。
            product.setFirstProductImage(new ProductImage());
        }
    }

    public void setFirstProductImages(List<Product> products) {
        for (Product product : products) {
            setFirstProductImage(product);
        }
    }

    public void setFirstProdutImagesOnOrderItems(List<OrderItem> ois) {
        for (OrderItem orderItem : ois) {
            setFirstProductImage(orderItem.getProduct());
        }
    }

}
