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
    /**
     * 根据订单查询订单项信息
     * @param order
     * @return
     */
    List<OrderItem> findByOrderOrderByIdDesc(Order order);

    /**
     * 根据产品查询订单项信息
     * @param product
     * @return
     */
    List<OrderItem> findByProduct(Product product);

    /**
     * 查找没有唯有订单id的订单项记录
     * @param  user
     * @return
     */
    List<OrderItem> findByUserAndOrderIsNull(User user);
}
