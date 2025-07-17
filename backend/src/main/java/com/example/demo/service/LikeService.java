package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.LikeRepository;
import com.example.demo.vo.ResultData;

@Service
public class LikeService {
	
	@Autowired
	private LikeRepository likeRepository;

	public LikeService(LikeRepository likeRepository) {
		this.likeRepository = likeRepository;
	}

	public int getLikes(int id) {
		return likeRepository.getLikes(id);
	}

	public ResultData insertLike(int loginedMemberId, int id) {
		
		int affectedRow = likeRepository.insertLike(loginedMemberId, id);
		
		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시글 없음", "id", id);
		}

		return ResultData.from("S-1", "좋아요 확인", "articleId", id);
	}

	public ResultData deleteLike(int loginedMemberId, int id) {
		
		int affectedRow = likeRepository.deleteLike(loginedMemberId, id);
		
		if (affectedRow == 0) {
			return ResultData.from("F-1", "해당 게시글 없음", "id", id);
		}

		return ResultData.from("S-1", "좋아요 취소", "articleId", id);
		
	}

	public boolean isMyLike(int loginedMemberId, int id) {
		
		if(likeRepository.isMyLike(loginedMemberId, id) == 1) return true;
		else return false;
	}

}
