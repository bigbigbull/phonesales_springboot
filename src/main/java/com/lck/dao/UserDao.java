package com.lck.dao;

import com.lck.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/22
 */
public interface UserDao extends JpaRepository<User,Integer> {

    /**
     * 根据用户名查询用户信息（用于注册）
     * @param name
     * @return
     */
    User findByName(String name);

    /**
     * 根据用户名和密码查询用户信息（用于登录）
     * @param name
     * @param password
     * @return
     */
    User getByNameAndPassword(String name, String password);
}
