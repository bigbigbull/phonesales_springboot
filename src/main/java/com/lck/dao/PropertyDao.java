package com.lck.dao;

import com.lck.pojo.Category;
import com.lck.pojo.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/11/29
 */
public interface PropertyDao extends JpaRepository<Property,Integer>{
    /**
     * 获取属性集合
     * @param category
     * @param pageable
     * @return Page
     *
     */
    Page<Property> findByCategory(Category category, Pageable pageable);

    /**
     * 通过分类获取所有属性
     * @param category
     * @return List
     */
    List<Property> findByCategory(Category category);
}
