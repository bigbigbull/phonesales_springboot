package com.lck.controller;

import com.lck.pojo.Product;
import com.lck.service.ProductImageService;
import com.lck.service.ProductService;
import com.lck.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/12/05
 */
@RestController
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductImageService productImageService;

    @GetMapping("/categories/{cid}/products")
    public Page4Navigator<Product> list(@PathVariable("cid") Integer cid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception{
        start = start<0?0:start;
        Page4Navigator<Product> page =productService.list(cid, start, size,5 );
        productImageService.setFirstProductImages(page.getContent());
        return page;
    }

    @GetMapping("/products/{id}")
    public Product get(@PathVariable("id") Integer id) throws Exception{
        Product product = productService.get(id);
        return product;
    }

    @PostMapping("/products")
    public Object add(@RequestBody Product product) throws Exception{
        product.setCreateDate(new Date());
        productService.add(product);
        return product;
    }

    @DeleteMapping("/products/{id}")
    public String delete(@PathVariable("id") Integer id) throws Exception{
         productService.delete(id);
         return null;
    }

    @PutMapping("/products")
    public Object update(@RequestBody Product product) throws Exception{
        productService.update(product);
        return product;
    }
}
