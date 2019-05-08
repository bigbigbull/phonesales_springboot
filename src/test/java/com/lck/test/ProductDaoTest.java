package com.lck.test;

import com.lck.Application;
import com.lck.dao.ProductDao;
import com.lck.pojo.Product;
import com.lck.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * describe：测试Jpa增加方法的返回值
 *
 * @author lichangkai
 * @date 2019/05/08
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProductDaoTest {
    @Autowired
    ProductDao productDao;

    @Test
    public void test() {
        Product product = new Product();
        product.setName("华为荣耀100");
        Product product1 = productDao.save(product);
        System.out.println(product1.getId());
    }
}
