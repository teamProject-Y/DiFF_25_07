package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UsrHomeController {

	private static final Logger logger = LoggerFactory.getLogger(UsrHomeController.class);

	@RequestMapping("/usr/home/main")
	public String showMain() {
		return "usr/home/main";
	}
	
	@RequestMapping("/")
	public String connectMain() {
		return "redirect:/usr/home/main";
	}

	@GetMapping("/usr/test")
	public Map<String, Object> testHandler() {

		logger.info("TEST 실행");

		Map<String, Object> res = new HashMap<>();
		res.put("SUCCESS", true);
		res.put("SUCCESS_TEXT", "Hello SpringBoot/React");

		return res;
	}


}
