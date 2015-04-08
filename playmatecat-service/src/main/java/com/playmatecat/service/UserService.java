package com.playmatecat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	public User selectUserById(int id) {
		return userMapper.selectById(id);
	}
	
	public int insertUser(User user) {
		return userMapper.insertUser(user);
	}
}
