package com.lck.dao;

import com.lck.pojo.Order;
import com.lck.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/26
 */
public interface OrderDao extends JpaRepository<Order, Integer> {
    /**
     * 查询不是删除状态下的订单信息
     *
     * @param status
     * @param user
     * @return List
     */
    List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}
