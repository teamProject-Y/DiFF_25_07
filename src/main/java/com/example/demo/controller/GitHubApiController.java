package com.example.demo.controller;

import com.example.demo.service.GitHubService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GitHubApiController {

    private final GitHubService gitHubService;

    public GitHubApiController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/api/github/repos")
    public List<Map> getRepos(
            @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient
    ) {
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        System.out.println(
                "accessToken: " + accessToken
        );
        return gitHubService.fetchGitHubRepos(accessToken);
    }
}
