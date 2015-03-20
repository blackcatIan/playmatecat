package com.playmatecat.cpt;

import net.minidev.json.JSONValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.playmatecat.service.User;
import com.playmatecat.service.UserService;

@Component
public class UserCpt {
	
	@Autowired
	private UserService userService;
	
	public String testCall() {
		Long start = System.currentTimeMillis();
		User user = userService.selectUserById(100000000);
		Long end = System.currentTimeMillis();
		System.out.println("sql finshed in " + (end - start) + " ms");
		
		return JSONValue.toJSONString(user);
	}
}

