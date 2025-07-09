package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeRepository {

	int getLikes(int id);

	int insertLike(int loginedMemberId, int id);

	int deleteLike(int loginedMemberId, int id);

	int isMyLike(int loginedMemberId, int id);

}