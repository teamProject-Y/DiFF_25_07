package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

@Controller
public class SonarUploadController {

    @PostMapping("/upload-source")
    @ResponseBody
    public String uploadSource(@RequestParam("file") MultipartFile zipFile) throws IOException {
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
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    newFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
            }
        }
    }

    private void createSonarPropertiesFile(File projectDir) throws IOException {
        File propertiesFile = new File(projectDir, "sonar-project.properties");
        try (PrintWriter writer = new PrintWriter(propertiesFile)) {
            writer.println("sonar.projectKey=Diff");
            writer.println("sonar.projectName=Diff");
            writer.println("sonar.projectVersion=1.0");
            writer.println("sonar.sources=.");
            writer.println("sonar.java.source=17");
            writer.println("sonar.host.url=http://localhost:9000");
            writer.println("sonar.login=YOUR_SONAR_TOKEN"); // 🔁 여기 사용자 토큰으로 변경
        }
    }

    private void runSonarScanner(File projectDir) throws IOException {
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
    }
}
