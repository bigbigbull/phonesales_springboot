package com.lck.dao;

import com.lck.pojo.Product;
import com.lck.pojo.Property;
import com.lck.pojo.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/02/01
 */
public interface PropertyValueDao extends JpaRepository<PropertyValue,Integer>{
    /**
     * 产品查询
     * @param product
     * @return List
     */
    List<PropertyValue> findByProductOrderByIdDesc(Product product);

    /**
     * 根据产品和属性获取属性值
     * @param property
     * @param product
     * @return  PropertyValue
     */
     PropertyValue getByPropertyAndProduct(Property property,Product product);

     /**
      * 根据产品删除对应的属性值
      * @param pid
      * @return
      */

     @Query(value = "DELETE FROM propertyvalue WHERE pid= ?1 ", nativeQuery = true)
     @Modifying
     void deleteByProductId(int pid);

    /**
     * 根据属性删除对应的属性值
     * @param ptid
     * @return
     */
    @Query(value = "DELETE FROM propertyvalue WHERE ptid= ?1 ", nativeQuery = true)
    @Modifying
    void deleteByPropertyId(int ptid);

}
