package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.FaqService;
import com.example.demo.vo.Faq;
import com.example.demo.vo.ResultData;

@RestController
public class UsrHomeApiController {

	@Autowired
	private FaqService faqService;  // Faq는 이미 다른 곳에서 구현되어 있다고 했으니 그대로 사용

	/**
	 * GET  /api/faqs
	 * q 파라미터로 질문을 받으면 FAQ를 조회해서 JSON으로 리턴
	 */
	@GetMapping("/api/faqs")
	public ResultData<Faq> getFaq(@RequestParam(name="q", required=false) String question) {
		if (question == null || question.isBlank()) {
			return ResultData.from("F-1", "질문(q)을 입력해주세요.");
		}

		Faq faq = faqService.getFaq(question);
		if (faq == null) {
			faq = new Faq(question, "해당 질문에 대한 FAQ 답변은 등록되지 않았습니다.");
			return ResultData.from("S-2", "등록된 FAQ가 없습니다.");
		}

		return ResultData.from("S-1", "성공");
	}
}
