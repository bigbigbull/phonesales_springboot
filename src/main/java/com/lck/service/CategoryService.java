package com.lck.service;

import com.lck.dao.CategoryDao;
import com.lck.pojo.Category;
import com.lck.pojo.Product;
import com.lck.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/11/29
 */
@Service
public class CategoryService {
    @Autowired
    CategoryDao categoryDao;

    public List<Category> list(){
        // 首先创建一个 Sort 对象，表示通过 id 倒排序， 然后通过 categoryDAO进行查询。
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return categoryDao.findAll(sort);
    }


    public Page4Navigator<Category> list(int start,int size,int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page pageFromJPA = categoryDao.findAll(pageable);
        return new Page4Navigator<Category>(pageFromJPA,navigatePages);
    }

    /**
     * 增加
     * */
    public void add(Category category){
        categoryDao.save(category);
    }

    public void  delete(Integer id){
        categoryDao.delete(id);
    }

    /**
     * 编辑
     * */
    public Category get(Integer id){
        Category category = categoryDao.findOne(id);
        return category;
    }

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
