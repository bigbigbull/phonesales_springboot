package com.lck.service;

import com.lck.dao.ProductDao;
import com.lck.dao.PropertyValueDao;
import com.lck.es.ProductEsDao;
import com.lck.pojo.Category;
import com.lck.pojo.Product;
import com.lck.util.Page4Navigator;
import com.lck.util.RestPage;
import com.lck.util.SpringContextUtil;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
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
@CacheConfig(cacheNames = "products")
public class ProductService {
    @Autowired
    ProductDao productDao;

    @Autowired
    ProductEsDao productEsDao;

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

    /**
     * 初始化数据到elasticsearch
     *
     * @param
     * @return
     */
    private void initDataBaseToEs() {
        Pageable pageable = new PageRequest(0, 5);
        Page<Product> page = productEsDao.findAll(pageable);
        if (page.getContent().isEmpty()) {
            List<Product> products = productDao.findAll();
            products.forEach(item -> productEsDao.save(item));
        }
    }


    /**
     *  将返回的有id的数据再次存入数据库
     *
     * @param  product
     * @return
     */
    @CacheEvict(allEntries = true)
    public void add(Product product) {
        Product product1 = productDao.save(product);

        // 同步到elasticsearch
        productEsDao.save(product1);
    }

    /**
     * 删除产品时，同时级联删除其对应属性值表中的数据
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        productDao.delete(id);
        // 同步到elasticsearch
        productEsDao.delete(id);
        propertyValueDao.deleteByProductId(id);
    }

    @Cacheable(key = "'products-one-'+#p0")
    public Product get(Integer id) {
        return productDao.findOne(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Product product) {
        productDao.save(product);
        productEsDao.save(product);
    }

    @Cacheable(key = "'products-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
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
       /* 因为 springboot 的缓存机制是通过切面编程 aop来实现的。
        直接调用 listByCategory 方法， aop 是拦截不到的，也就不会走缓存了*/
        ProductService productService = SpringContextUtil.getBean(ProductService.class);
        List<Product> products = productService.listByCategory(category);
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
    @Cacheable(key = "'products-cid-'+#p0.id")
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

    /**
     * 使用elasticsearch查询
     *
     * @param keyword
     * @param start
     * @param size
     * @return List
     */
    public List<Product> search(String keyword, int start, int size) {
      
        initDataBaseToEs();
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(QueryBuilders.matchPhraseQuery("name", keyword),
                        ScoreFunctionBuilders.weightFactorFunction(100))
                .scoreMode("sum")
                .setMinScore(10);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();
        Page<Product> page = productEsDao.search(searchQuery);
        return page.getContent();
    }


}