package com.playmatecat.cpt;

import java.util.Random;

import net.minidev.json.JSONValue;

import org.apache.commons.lang3.RandomUtils;
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
		User user = new User();
		
		userService.selectUserById(100000000);
		Long end = System.currentTimeMillis();
		//System.out.println("sql finshed in " + (end - start) + " ms");

		user.setUsername(String.valueOf(RandomUtils.nextLong(0, 100000)));
		
		
		return JSONValue.toJSONString(user);
	}
}

