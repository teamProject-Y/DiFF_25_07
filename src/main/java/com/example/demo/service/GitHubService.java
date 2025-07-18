//package com.example.demo.service;
//
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class GitHubService {
//
//    public List<Map> fetchGitHubRepos(String accessToken) {
//        WebClient webClient = WebClient.builder()
//                .baseUrl("https://api.github.com")
//                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
//                .build();
//
//        return webClient.get()
//                .uri("/user/repos")
//                .retrieve()
//                .bodyToFlux(Map.class)
//                .collectList()
//                .block();
//    }
//    // GitHubService.java
//    public List<Map<String, Object>> getCommits(String owner, String repo, String token) {
//        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/commits";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                entity,
//                new ParameterizedTypeReference<>() {}
//        );
//
//        return response.getBody(); // 여기서 commit 메시지, sha, 작성자 등 추출 가능
//    }
//
//    public Map<String, Object> getCommitDetail(String owner, String repo, String sha, String token) {
//        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/commits/" + sha;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                entity,
//                new ParameterizedTypeReference<>() {}
//        );
//
//        return response.getBody(); // 여기서 파일 리스트, diff, 변경 타입 추출 가능
//    }
//
//}
