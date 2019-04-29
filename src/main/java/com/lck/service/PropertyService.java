package com.lck.service;

import com.lck.dao.PropertyDao;
import com.lck.dao.PropertyValueDao;
import com.lck.pojo.Category;
import com.lck.pojo.Property;
import com.lck.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class PropertyService {

    @Autowired
    PropertyDao propertyDao;
    @Autowired
    PropertyValueDao propertyValueDao;

    @Autowired
    CategoryService categoryService;

    public void add(Property bean){
        propertyDao.save(bean);
    }

    /**
     * 级联删除
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id){
        propertyDao.delete(id);
        propertyValueDao.deleteByPropertyId(id);
    }

    public Property get(Integer id){
        return propertyDao.findOne(id);
    }

    public void update(Property bean){
        propertyDao.save(bean);
    }

    public Page4Navigator<Property> list(Integer cid,int start,int size,int navigatePages){
        Category category = categoryService.get(cid);

        Sort sort = new Sort(Sort.Direction.DESC,"id");

        Pageable pageable = new PageRequest(start, size, sort);

        Page<Property> pageFromJPA =propertyDao.findByCategory(category,pageable);

        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    public List<Property> listByCategory(Category category){
        return propertyDao.findByCategory(category);
    }
}
