package com.example.demo.repository;

import com.example.demo.vo.Commit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommitRepository {
    void save(Commit newCommit);

    boolean existsByCommitHash(String sha);
}
