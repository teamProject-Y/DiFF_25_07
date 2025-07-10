package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.FaqService;
import com.example.demo.vo.Faq;
import com.example.demo.vo.ResultData;

@Controller
public class UsrHomeController {
	
	@Autowired
	FaqService faqservice;
	
	@RequestMapping("/usr/home/main")
	public String showMain() {
		return "usr/home/main";
	}
	
	@RequestMapping("/")
	public String connectMain() {
		return "redirect:/usr/home/main";
	}
	
	@RequestMapping("/usr/home/faq")
	public String faq(Model model, String question) {
		
		Faq faq;
		
		if(question != null) {
			faq = faqservice.getFaq(question);	
			if(faq == null) {
				faq = new Faq(question,"해당 질문에 대한 FAQ 답변은 등록되지 않았습니다.");
			}
			model.addAttribute("faq", faq);
		}
		model.addAttribute("question", question);
		
		return "/usr/home/faq";
	}

}
