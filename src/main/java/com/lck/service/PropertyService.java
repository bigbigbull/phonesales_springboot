package com.lck.service;

import com.lck.dao.PropertyDao;
import com.lck.dao.PropertyValueDao;
import com.lck.pojo.Category;
import com.lck.pojo.Property;
import com.lck.util.Page4Navigator;
import com.lck.util.RestPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/11/29
 */
@Service
@CacheConfig(cacheNames="properties")
public class PropertyService {

    @Autowired
    PropertyDao propertyDao;
    @Autowired
    PropertyValueDao propertyValueDao;

    @Autowired
    CategoryService categoryService;

    @CacheEvict(allEntries=true)
    public void add(Property bean){
        propertyDao.save(bean);
    }

    /**
     * 级联删除
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries=true)
    public void delete(Integer id){
        propertyDao.delete(id);
        propertyValueDao.deleteByPropertyId(id);
    }

    @Cacheable(key="'properties-one-'+#p0")
    public Property get(Integer id){
        return propertyDao.findOne(id);
    }

    @CacheEvict
    public void update(Property bean){
        propertyDao.save(bean);
    }

    @Cacheable(key = "'properties-cid-'+#p0+'-page-'+#p1+'-'+#p2")
    public Page4Navigator<Property> list(Integer cid,int start,int size,int navigatePages){
        Category category = categoryService.get(cid);

        Sort sort = new Sort(Sort.Direction.DESC,"id");

        Pageable pageable = new PageRequest(start, size, sort);

        Page<Property> pageFromJPA =propertyDao.findByCategory(category,pageable);

        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Cacheable(key = "'properties-cid-'+#p0.id")
    public List<Property> listByCategory(Category category){
        return propertyDao.findByCategory(category);
    }
}
