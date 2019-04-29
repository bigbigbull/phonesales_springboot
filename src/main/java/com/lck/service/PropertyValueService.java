package com.lck.service;

import com.lck.dao.PropertyValueDao;
import com.lck.pojo.Product;
import com.lck.pojo.Property;
import com.lck.pojo.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/02/01
 */
@Service
public class PropertyValueService {
    @Autowired
    PropertyValueDao propertyValueDao;

    @Autowired
    PropertyService propertyService;

    public void update(PropertyValue propertyValue){
        propertyValueDao.save(propertyValue);
    }

    public void init(Product product){
        List<Property> propertyList = propertyService.listByCategory(product.getCategory());
        propertyList.forEach(property -> {
            PropertyValue propertyValue =  getByPropertyAndProduct(product,property);
            if(null == propertyValue){
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValueDao.save(propertyValue);
            }
        });
    }

    public PropertyValue getByPropertyAndProduct(Product product, Property property) {
        return propertyValueDao.getByPropertyAndProduct(property,product);
    }

    public List<PropertyValue> list(Product product) {
        return propertyValueDao.findByProductOrderByIdDesc(product);
    }
    

}
