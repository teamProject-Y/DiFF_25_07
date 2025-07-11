package com.example.demo.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GitHubService {

    public List<Map> fetchGitHubRepos(String accessToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .build();

        return webClient.get()
                .uri("/user/repos")
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();
    }
}
