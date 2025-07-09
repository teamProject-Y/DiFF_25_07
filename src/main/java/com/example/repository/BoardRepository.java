package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.vo.Board;

@Mapper
public interface BoardRepository {

	public Board getBoardById(int id);

}
