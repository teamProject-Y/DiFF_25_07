package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.io.*;

@Service
public class SonarQubeService {

    @Value("${sonarqube.host}")
    private String sonarHost;

    @Value("${sonarqube.token}")
    private String sonarToken;

    public String getAnalysisResult(String projectKey) {
        System.out.println("소나 토큰 : "+sonarToken);
        String url = sonarHost + "/api/measures/component?component=" + projectKey
                + "&metricKeys=bugs,vulnerabilities,code_smells,coverage";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(sonarToken, "");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    public void deleteProject(String projectKey) {
        String url = sonarHost + "/api/projects/delete?project=" + projectKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(sonarToken, "");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    public String analyzeProject(File projectDir, String projectKey) throws IOException {
        try {
            createSonarPropertiesFile(projectDir, projectKey);

            System.out.println("📁 Sonar 분석 디렉토리: " + projectDir.getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder("sonar-scanner");
            pb.directory(projectDir);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("▶ " + line);
                }
            }

            Thread.sleep(3000); // 결과 수신 대기

            String resultJson = getAnalysisResult(projectKey);
            System.out.println("📊 분석 결과: " + resultJson);

            deleteProject(projectKey); // ✅ SonarQube 서버에서 삭제
            System.out.println("🧹 Sonar 프로젝트 삭제 완료: " + projectKey);

            return resultJson;

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ 분석 실패: " + e.getMessage();

        } finally {
            deleteDirectoryRecursively(projectDir); // ✅ 로컬 디렉토리 삭제
            System.out.println("🧹 임시 디렉토리 삭제 완료: " + projectDir.getAbsolutePath());
        }
    }

    private void deleteDirectoryRecursively(File dir) {
        if (dir == null || !dir.exists()) return;

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryRecursively(file);
                } else {
                    file.delete();
                }
            }
        }
        dir.delete();
    }


    private void createSonarPropertiesFile(File projectDir, String projectKey) throws IOException {
        File propertiesFile = new File(projectDir, "sonar-project.properties");

        // 기본값
        String sourcePath = "src";
        String binaryPath = "target/classes";

        // 경로 자동 감지
        if (new File(projectDir, "src/main/java").exists()) {
            sourcePath = "src/main/java";
        }

        if (new File(projectDir, "build/classes/java/main").exists()) {
            binaryPath = "build/classes/java/main";
        }

        try (PrintWriter writer = new PrintWriter(propertiesFile)) {
            writer.println("sonar.projectKey=" + projectKey);
            writer.println("sonar.projectName=" + projectKey);
            writer.println("sonar.projectVersion=1.0");
            writer.println("sonar.sources=" + sourcePath);
            writer.println("sonar.java.binaries=" + binaryPath);
            writer.println("sonar.java.source=17");
            writer.println("sonar.login=" + sonarToken);  // 토큰 추가

        }
    }
}
