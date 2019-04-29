package com.lck.service;

import com.lck.dao.OrderItemDao;
import com.lck.pojo.Order;
import com.lck.pojo.OrderItem;
import com.lck.pojo.Product;
import com.lck.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/26
 */
@Service
public class OrderItemService {
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    ProductImageService productImageService;

    public void fill(List<Order> orders) {
        for (Order order : orders) {
            fill(order);
        }
    }

    public void fill(Order order) {
        List<OrderItem> orderItems = listByOrder(order);
        float total = 0;
        int totalNumber = 0;
        for (OrderItem oi : orderItems) {
            total += oi.getNumber() * oi.getProduct().getPromotePrice();
            totalNumber += oi.getNumber();
            productImageService.setFirstProductImage(oi.getProduct());
        }
        order.setTotal(total);
        order.setOrderItems(orderItems);
        order.setTotalNumber(totalNumber);
    }

    public List<OrderItem> listByOrder(Order order) {
        return orderItemDao.findByOrderOrderByIdDesc(order);
    }

    public void add(OrderItem orderItem) {
        orderItemDao.save(orderItem);
    }

    public void update(OrderItem orderItem) {
        orderItemDao.save(orderItem);
    }

    public OrderItem get(int id) {
        return orderItemDao.findOne(id);
    }

    public void delete(int id) {
        orderItemDao.delete(id);
    }

    public int getSaleCount(Product product) {
        List<OrderItem> ois = listByProduct(product);
        int result = 0;
        for (OrderItem oi : ois) {
            if (null != oi.getOrder()) {
                if (null != oi.getOrder() && null != oi.getOrder().getPayDate()) {
                    result += oi.getNumber();
                }
            }
        }
        return result;
    }

    public List<OrderItem> listByProduct(Product product) {
        return orderItemDao.findByProduct(product);
    }

    public List<OrderItem> listByUser(User user) {
        return orderItemDao.findByUserAndOrderIsNull(user);
    }
}