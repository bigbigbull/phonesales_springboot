package com.lck.service;

import com.lck.dao.UserDao;
import com.lck.pojo.User;
import com.lck.util.Page4Navigator;
import com.lck.util.RestPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/22
 */
@Service
@CacheConfig(cacheNames = "users")
public class UserService {
    @Autowired
    UserDao userDao;

    @Cacheable(key = "'users-page-'+#p0+'-'+#p1")
    public Page4Navigator<User> list(int start,int size,int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page pageFromJPA = userDao.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    public boolean isExist(String name) {
        User user = getByName(name);
        return null!=user;
    }

    @Cacheable(key = "'users-one-name-'+#p0")
    public User getByName(String name) {
        return userDao.findByName(name);
    }

    @Cacheable(key = "'users-one-name-'+#p0+'-password-'+#p1")
    public User get(String name, String password) {
        return userDao.getByNameAndPassword(name,password);
    }

    @CacheEvict(allEntries = true)
    public void add(User user) {
        userDao.save(user);
    }
}
