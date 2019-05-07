package com.lck.service;

import com.lck.dao.PropertyValueDao;
import com.lck.pojo.Product;
import com.lck.pojo.Property;
import com.lck.pojo.PropertyValue;
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
 * @date 2019/02/01
 */
@Service
@CacheConfig(cacheNames = "PropertyValues")
public class PropertyValueService {
    @Autowired
    PropertyValueDao propertyValueDao;

    @Autowired
    PropertyService propertyService;

    @CacheEvict(allEntries=true)
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

    @Cacheable(key = "'PropertyValues-pid-'+#p0.id+'-ptid-'+#p1.id")
    public PropertyValue getByPropertyAndProduct(Product product, Property property) {
        return propertyValueDao.getByPropertyAndProduct(property,product);
    }

    @Cacheable(key ="'PropertyValues-pid-'+#p0.id" )
    public List<PropertyValue> list(Product product) {
        return propertyValueDao.findByProductOrderByIdDesc(product);
    }
    

}
