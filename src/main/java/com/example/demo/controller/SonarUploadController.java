package com.example.demo.controller;

import com.example.demo.service.SonarQubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.*;


@Controller
public class SonarUploadController {
    @Autowired
    private SonarQubeService sonarQubeService;
    @PostMapping("/analyzeZip")
    @ResponseBody
    public String uploadSource(@RequestParam("zipFile") MultipartFile zipFile) throws IOException {
        // 1. 임시 디렉토리 생성
        Path tempDir = Files.createTempDirectory("source-");
        File tempDirFile = tempDir.toFile();

        // 2. zip 파일 저장
        Path zipPath = tempDir.resolve("source.zip");
        zipFile.transferTo(zipPath.toFile());

        // 3. 압축 해제
        unzip(zipPath.toFile(), tempDirFile);

        // 4. sonar-project.properties 생성
        createSonarPropertiesFile(tempDirFile);

        // 5. SonarScanner 실행
        runSonarScanner(tempDirFile);

        return "✅ 분석 요청 완료 (콘솔에서 로그 확인)";
    }

    private void unzip(File zipFile, File destDir) throws IOException {
        if (!destDir.exists()) destDir.mkdirs();

        try (ZipFile zip = new ZipFile(zipFile)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File newFile = new File(destDir, entry.getName());

                if (entry.isDirectory()) {
                    newFile.mkdirs();
                    continue;
                }

                newFile.getParentFile().mkdirs();
                try (InputStream is = zip.getInputStream(entry);
                     FileOutputStream fos = new FileOutputStream(newFile)) {
                    is.transferTo(fos); // Java 9 이상
                }
            }
        }
    }

    private void createSonarPropertiesFile(File projectDir) throws IOException {
        File propertiesFile = new File(projectDir, "sonar-project.properties");

        // 기본값
        String sourcePath = "src";
        String binaryPath = "target/classes";

        // 경로 자동 감지
        if (new File(projectDir, "src/main/java").exists()) {
            sourcePath = "src/main/java";
        } else if (new File(projectDir, "src").exists()) {
            sourcePath = "src";
        }

        if (new File(projectDir, "build/classes/java/main").exists()) {
            binaryPath = "build/classes/java/main";
        } else if (new File(projectDir, "target/classes").exists()) {
            binaryPath = "target/classes";
        }

        try (PrintWriter writer = new PrintWriter(propertiesFile)) {
            writer.println("sonar.projectKey=Diff");
            writer.println("sonar.projectName=Diff");
            writer.println("sonar.projectVersion=1.0");

            writer.println("sonar.sources=" + sourcePath);
            writer.println("sonar.java.binaries=" + binaryPath);
            writer.println("sonar.java.source=17");


        }
    }

    private void runSonarScanner(File projectDir) throws IOException {
        // 🔍 디버깅용 로그 추가
        System.out.println("📁 Sonar 분석 디렉토리: " + projectDir.getAbsolutePath());
        System.out.println("📄 properties 존재함? " + new File(projectDir, "sonar-project.properties").exists());

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

        String projectKey = "DiFF"; // TODO: 나중에 사용자 ID + 커밋 ID로 동적 생성

        try {
            Thread.sleep(3000); // 분석 완료 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String resultJson = sonarQubeService.getAnalysisResult(projectKey);
        System.out.println("📊 분석 결과: " + resultJson);

        // TODO: resultJson 파싱해서 DB 저장

        sonarQubeService.deleteProject(projectKey);
        System.out.println("🧹 Sonar 프로젝트 삭제 완료: " + projectKey);
        System.out.println("🧹 Sonar 프로젝트 삭제 완료: " + projectKey);
    }


}