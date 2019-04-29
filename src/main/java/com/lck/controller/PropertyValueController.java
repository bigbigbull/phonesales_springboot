package com.lck.controller;

import com.lck.pojo.Product;
import com.lck.pojo.PropertyValue;
import com.lck.service.ProductService;
import com.lck.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/12
 */
@RestController
public class PropertyValueController {
    @Autowired
    PropertyValueService propertyValueService;

    @Autowired
    ProductService productService;

    @GetMapping("/products/{pid}/propertyValues")
    public List<PropertyValue> list(@PathVariable("pid") int pid) {
        Product product = productService.get(pid);
        propertyValueService.init(product);
        List<PropertyValue> propertyValues = propertyValueService.list(product);
        return propertyValues;
    }

    @PutMapping("/propertyValues")
    public PropertyValue update(@RequestBody PropertyValue propertyValue) {
        propertyValueService.update(propertyValue);
        return propertyValue;
    }

  
}
