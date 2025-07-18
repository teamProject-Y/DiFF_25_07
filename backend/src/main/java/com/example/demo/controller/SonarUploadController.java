package com.example.demo.controller;

import com.example.demo.service.SonarQubeService;
import com.example.demo.service.SonarService;
import com.example.demo.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class SonarUploadController {

    @Autowired
    private SonarQubeService sonarQubeService;

    @Autowired
    private SonarService sonarService;

    @Autowired
    private Rq rq;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> uploadSource(@RequestParam("file") MultipartFile zipFile) {
        try {
            // 1. 사용자 및 커밋 기반 projectKey 생성
            Long memberId = rq.getLoginedMemberId();
            String commitId = UUID.randomUUID().toString();
            String projectKey = "temp_" + memberId + "_" + commitId;

            System.out.println("👤 사용자 ID: " + memberId);
            System.out.println("📂 생성된 Project Key: " + projectKey);

            // 2. 압축 해제 및 sonar-project.properties 생성
            String extractedPath = sonarService.extractAndPrepare(zipFile, projectKey);
            System.out.println("📦 압축 해제 위치: " + extractedPath);

            // 3. 분석 실행
            sonarService.runSonarScanner(extractedPath);

            // 4. 결과 조회
            String result = sonarQubeService.getAnalysisResult(projectKey);
            System.out.println("📊 분석 결과: " + result);

            // 5. SonarQube 프로젝트 삭제
            sonarQubeService.deleteProject(projectKey);
            System.out.println("🧹 SonarQube 프로젝트 삭제 완료: " + projectKey);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("❌ 분석 중 오류 발생: " + e.getMessage());
        }
    }
}
