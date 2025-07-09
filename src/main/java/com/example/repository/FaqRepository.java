package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.vo.Faq;

@Mapper
public interface FaqRepository {

	public Faq getFaq(String question);

}