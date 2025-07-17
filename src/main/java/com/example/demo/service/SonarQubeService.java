package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class SonarQubeService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${sonarqube.host}")
    private String sonarHost;

    @Value("${sonarqube.token}")
    private String sonarToken;

    public String getAnalysisResult(String projectKey) {
        String url = sonarHost + "/api/measures/component?component=" + projectKey
                + "&metricKeys=bugs,vulnerabilities,code_smells,coverage";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(sonarToken, "");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public void deleteProject(String projectKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(sonarToken, "");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("project", projectKey);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        restTemplate.postForEntity(sonarHost + "/api/projects/delete", request, String.class);
    }



}
