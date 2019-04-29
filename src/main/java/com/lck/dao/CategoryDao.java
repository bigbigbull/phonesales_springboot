package com.lck.dao;

import com.lck.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * describe:继承JpaRepository类，该类已封装了基本的CRUD
 *
 * @author lichangkai
 * @date 2018/11/29
 */
public interface CategoryDao extends JpaRepository<Category,Integer>{

}
