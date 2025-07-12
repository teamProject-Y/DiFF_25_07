package com.example.demo.repository;

import com.example.demo.vo.Repository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RepositoryRepository {
    void save(Repository repository);
    Repository findByGithubIdAndMemberId(Long githubId, Long memberId);
}