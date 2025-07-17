package com.example.demo.service;

import com.example.demo.vo.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class RepositoryService {

    public List<Repository> convertGitHubRepoMapToEntity(List<Map> githubRepoList, Long loginedMemberId) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        return githubRepoList.stream().map(repoMap -> {
            return Repository.builder()
                    .memberId(loginedMemberId)
                    .githubId(((Number) repoMap.get("id")).longValue())
                    .title((String) repoMap.get("name"))
                    .url((String) repoMap.get("html_url"))
                    .lastRequestCommitId(0L)
                    .delStatus(false)
                    .delDate(null)
                    .regDate(LocalDateTime.parse((String) repoMap.get("created_at"), formatter))
                    .updateDate(LocalDateTime.parse((String) repoMap.get("updated_at"), formatter))
                    .build();
        }).toList();
    }
}
