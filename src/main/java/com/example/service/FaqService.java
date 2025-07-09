package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.FaqRepository;
import com.example.vo.Faq;

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