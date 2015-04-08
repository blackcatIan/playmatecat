package com.playmatecat.service;


public interface UserMapper {
	public User selectById(int id);
	
	public int insertUser(User user);
}
