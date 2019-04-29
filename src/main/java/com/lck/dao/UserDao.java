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
    User findByName(String name);

    User getByNameAndPassword(String name, String password);
}
