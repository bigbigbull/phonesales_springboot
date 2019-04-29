package com.lck.service;

import com.lck.dao.ProductImageDao;
import com.lck.enums.ProductImageTypeEnum;
import com.lck.pojo.OrderItem;
import com.lck.pojo.Product;
import com.lck.pojo.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/12/20
 */
@Service
public class ProductImageService {
    @Autowired
    ProductImageDao productImageDao;
    @Autowired
    ProductService productService;

    public void add(ProductImage productImage){
        productImageDao.save(productImage);
    }

    public void delete(Integer id){
        productImageDao.delete(id);
    }

    public ProductImage get(Integer id){
        return productImageDao.findOne(id);
    }

    public List<ProductImage> listSingleProductImages(Product product){
        return productImageDao.findByProductAndTypeOrderByIdDesc(product, ProductImageTypeEnum.TYPE_SINGLE.toString());
    }

    public List<ProductImage> listDetailProductImages(Product product){
         return  productImageDao.findByProductAndTypeOrderByIdDesc(product,ProductImageTypeEnum.TYPE_DETAIL.toString());
    }

    public void setFirstProductImage(Product product){
        List<ProductImage> singleImages = listSingleProductImages(product);
        if(!singleImages.isEmpty()){
            product.setFirstProductImage(singleImages.get(0));
        }else {
            // 这样做是考虑到产品还没有来得及设置图片，但是在订单后台管理里查看订单项的对应产品图片。
            product.setFirstProductImage(new ProductImage());
        }
    }

    public void setFirstProductImages(List<Product> products){
        for (Product product:products){
           setFirstProductImage(product);
        }
    }
    
    public void setFirstProdutImagesOnOrderItems(List<OrderItem> ois) {
        for (OrderItem orderItem : ois) {
            setFirstProductImage(orderItem.getProduct());
        }
    }
    
}
