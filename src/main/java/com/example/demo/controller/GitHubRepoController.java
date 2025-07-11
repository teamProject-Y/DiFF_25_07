package com.example.demo.controller;

import com.example.demo.service.GitHubAuthService;
import com.example.demo.service.GitHubService;
import com.example.demo.vo.GitHubAuth;
import com.example.demo.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GitHubRepoController {

    @Autowired
    private Rq rq;

    @Autowired
    private GitHubAuthService gitHubAuthService;

    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/github/repos")
    public List<Map> getUserRepos() {
        int memberId = rq.getLoginedMemberId();

        GitHubAuth auth = gitHubAuthService.getLatestTokenByMemberId(memberId);
        String accessToken = auth.getAccessToken();

        return gitHubService.fetchGitHubRepos(accessToken);
    }
}
