package com.example.demo.repository;

import com.example.demo.vo.Repository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RepositoryRepository {
    void save(Repository repository);
    Repository findByGithubIdAndMemberId(Long githubId, Long memberId);

    List<Repository> findByMemberId(Long memberId);
}