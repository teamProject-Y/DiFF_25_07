package com.example.demo.controller;

import com.example.demo.service.GitHubAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubAuthController {

    @Autowired
    private GitHubAuthService gitHubAuthService;

    @GetMapping("/api/github/save-token")
    public String saveGitHubToken(
            @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient,
            OAuth2AuthenticationToken authentication
    ) {
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        String tokenType = authorizedClient.getAccessToken().getTokenType().getValue();
        String scope = String.join(",", authorizedClient.getAccessToken().getScopes());


        Long memberId = 1L;

        gitHubAuthService.saveGitHubToken(memberId, accessToken, tokenType, scope);
        return "GitHub token saved for memberId = " + memberId;
    }
}
