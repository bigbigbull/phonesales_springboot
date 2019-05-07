package com.lck.service;

import com.lck.dao.CategoryDao;
import com.lck.pojo.Category;
import com.lck.pojo.Product;
import com.lck.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/11/29
 */
@Service
@CacheConfig(cacheNames = "categories")
public class CategoryService {
    @Autowired
    CategoryDao categoryDao;

    @Cacheable(key = "'categories-all'")
    public List<Category> list(){
        // 首先创建一个 Sort 对象，表示通过 id 倒排序， 然后通过 categoryDAO进行查询。
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        return categoryDao.findAll(sort);
    }

    /**
     * 在redis中保存一个json数组
     * */
    @Cacheable(key = "'categories-page-'+#p0+ '-' + #p1")
    public Page4Navigator<Category> list(int start,int size,int navigatePages){

        Sort sort = new Sort(Sort.Direction.DESC,"id");

        Pageable pageable = new PageRequest(start,size,sort);

        Page pageFromJPA = categoryDao.findAll(pageable);

        return new Page4Navigator<Category>(pageFromJPA,navigatePages);
    }

    /**
     * 删除redis中关于categories的所有数据，牺牲小许性能，保证数据的一致性
     * 增加
     * */                                                                    
    @CacheEvict(allEntries=true)
    public void add(Category category){
        categoryDao.save(category);
    }

    @CacheEvict(allEntries=true)
    public void  delete(Integer id){
        categoryDao.delete(id);
    }

    /**
     * 编辑
     * 第一次访问的时候， redis 是不会有数据的，所以就会通过 jpa 到数据库里去取出来，
     * 一旦取出来之后，就会放在 redis里。 key 呢就是如图所示的 categories-one-xx这个 key。
     * 第二次访问的时候，redis 就有数据了，就不会从数据库里获取了。
     * */
    @Cacheable(key = "'categories-one-'+#p0")
    public Category get(Integer id){
        Category category = categoryDao.findOne(id);
        return category;
    }
    
    @CacheEvict(allEntries=true)
    public void update(Category category){
        categoryDao.save(category);
    }


    public void removeCategoryFromProduct(List<Category> cs) {
        for (Category category : cs) {
            removeCategoryFromProduct(category);
        }
    }

    /**
     * 不用注解忽略json化，结合Redis时有坑
     * 用于对产品做序列化的时候从产品中的分类移除，防止无限迭代
     * */
    public void removeCategoryFromProduct(Category category) {
        List<Product> products =category.getProducts();
        if(null!=products) {
            for (Product product : products) {
                product.setCategory(null);
            }
        }

        List<List<Product>> productsByRow =category.getProductsByRow();
        if(null!=productsByRow) {
            for (List<Product> ps : productsByRow) {
                for (Product p: ps) {
                    p.setCategory(null);
                }
            }
        }
    }
}
