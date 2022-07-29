package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByActAndPwd(map);
    }

    @Override
    public List<User> queryUsersAll() {
        return userMapper.selectUsersAll();
    }
}
