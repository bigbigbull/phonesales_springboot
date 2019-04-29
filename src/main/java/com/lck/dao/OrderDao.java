package com.lck.dao;

import com.lck.pojo.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/26
 */
public interface OrderDao extends JpaRepository<Order,Integer> {
}
