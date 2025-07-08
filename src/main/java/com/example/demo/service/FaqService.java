package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.FaqRepository;
import com.example.demo.vo.Board;
import com.example.demo.vo.Faq;

@Service
public class FaqService {

	@Autowired
	private FaqRepository faqRepository;

	public FaqService(FaqRepository faqRepository) {
		this.faqRepository = faqRepository;
	}

	public Faq getFaq(String question) {
		return faqRepository.getFaq(question);
	}


}