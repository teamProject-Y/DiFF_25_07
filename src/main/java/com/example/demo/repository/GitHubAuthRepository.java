package com.example.demo.repository;

import com.example.demo.vo.GitHubAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GitHubAuthRepository {
    void save(GitHubAuth auth);

    GitHubAuth findLatestByMemberId(int memberId);
}
