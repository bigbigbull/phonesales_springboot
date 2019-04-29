package com.lck.dao;

import com.lck.pojo.Order;
import com.lck.pojo.OrderItem;
import com.lck.pojo.Product;
import com.lck.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/26
 */
public interface OrderItemDao extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByOrderOrderByIdDesc(Order order);

    List<OrderItem> findByProduct(Product product);

    /**
     * 查找没有唯有订单id的订单项记录
     * @param
     * @return
     */
    List<OrderItem> findByUserAndOrderIsNull(User user);
}
