package com.playmatecat.cpt;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.playmatecat.service.User;
import com.playmatecat.service.UserService;

@Component
public class UserCpt {
	
	@Autowired
	private UserService userService;
	
	public String savetestCall() throws Exception{
		Long start = System.currentTimeMillis();
		User user = new User();
		
		userService.selectUserById(100000000);
		Long end = System.currentTimeMillis();
		//System.out.println("sql finshed in " + (end - start) + " ms");

		user.setUsername(String.valueOf(RandomUtils.nextLong(0, 100000)));
		
		User tmpUser = new User();
		user.setUsername("testTx");
		userService.insertUser(tmpUser);
		
//		if(StringUtils.isEmpty("")) {
//			throw new Exception("test tx error");
//		}
		
		
		return JSONValue.toJSONString(user);
	}
	
	
	public static void main(String[] args) {
		User user = new User();
		user.setUsername("abc");
		String jsonStr = JSONValue.toJSONString(user);
		JSONObject jsonObj = JSONValue.parse(jsonStr, JSONObject.class);
	}
}

