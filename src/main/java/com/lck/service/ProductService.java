package com.lck.service;

import com.lck.dao.ProductDao;
import com.lck.dao.PropertyValueDao;
import com.lck.pojo.Category;
import com.lck.pojo.Product;
import com.lck.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/12/05
 */
@Service
public class ProductService {
    @Autowired
    ProductDao productDao;

    @Autowired
    PropertyValueDao propertyValueDao;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    ReviewService reviewService;

    public void add(Product product) {
        productDao.save(product);
    }

    /**
     * 删除产品时，同时级联删除其对应属性值表中的数据
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        productDao.delete(id);
        propertyValueDao.deleteByProductId(id);
    }

    public Product get(Integer id) {
        return productDao.findOne(id);
    }

    public void update(Product product) {
        productDao.save(product);
    }

    public Page4Navigator<Product> list(Integer cid, int start, int size, int navigatePages) {
        Category category = categoryService.get(cid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Product> pageFormJpa = productDao.findByCategory(category, pageable);
        return new Page4Navigator<>(pageFormJpa, navigatePages);
    }

    /**
     * 为分类填充产品
     *
     * @param category
     * @return
     */
    public void fill(Category category) {
        List<Product> products = listByCategory(category);
        productImageService.setFirstProductImages(products);
        category.setProducts(products);
    }

    /**
     * 为多个分类填充产品集合
     *
     * @param categorys
     * @return
     */
    public void fill(List<Category> categorys) {
        for (Category category : categorys) {
            fill(category);
        }
    }

    /**
     * 为多个分类填充推荐产品集合，即把分类下的产品集合，按照8个为一行，拆成多行，以利于后续页面上进行显示
     *
     * @param categorys
     * @return
     */
    public void fillByRow(List<Category> categorys) {
        int productNumberEachRow = 8;
        for (Category category : categorys) {
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberEachRow) {
                int size = i + productNumberEachRow;
                size = size > products.size() ? products.size() : size;
                List<Product> productsOfEachRow = products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }
    }

    /**
     * 查询某个分类下的所有产品
     *
     * @param category
     * @return
     */
    public List<Product> listByCategory(Category category) {
        return productDao.findByCategoryOrderById(category);
    }

    public void setSaleAndReviewNumber(Product product) {
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);

        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);

    }

    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product : products) {
            setSaleAndReviewNumber(product);
        }
    }

    public List<Product> search(String keyword, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        List<Product> products = productDao.findByNameLike("%" + keyword + "%", pageable);
        return products;
    }

    
}