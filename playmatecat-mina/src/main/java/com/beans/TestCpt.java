package com.beans;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestCpt {
	
	@Autowired
	private TestService testService;
	
	public String testCall(User user) {
		
		JSONObject json = new JSONObject();
		
		return JSONValue.toJSONString(user);
	}
}
