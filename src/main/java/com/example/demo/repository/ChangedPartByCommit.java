package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChangedPartByCommit {
    void save(@Param("part") ChangedPartByCommit part);
}
