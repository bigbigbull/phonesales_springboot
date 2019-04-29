package com.lck.controller;

import com.lck.pojo.User;
import com.lck.service.UserService;
import com.lck.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * describe:
 *
 * @author lichangkai
 * @date 2019/04/22
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public Page4Navigator<User> list(@RequestParam(value = "start",defaultValue = "0")int start,@RequestParam(value = "size",defaultValue = "5") int size ){
        start =start<0?0:start;
        Page4Navigator<User> page =userService.list(start,size,5);
        return page;
    }
}
