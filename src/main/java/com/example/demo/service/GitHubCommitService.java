//package com.example.demo.service;
//
//import com.example.demo.repository.ChangedPartByCommit;
//import com.example.demo.repository.CommitRepository;
//import com.example.demo.repository.RepositoryRepository;
//import com.example.demo.vo.Commit;
//import com.example.demo.vo.GitHubAuth;
//import com.example.demo.vo.Repository;
//import com.example.demo.vo.ChangedPartByCommit;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class GitHubCommitService {
//
//    @Autowired
//    private GitHubService gitHubService;
//
//    @Autowired
//    private GitHubAuthService gitHubAuthService;
//
//    @Autowired
//    private RepositoryRepository repositoryRepository;
//
//    @Autowired
//    private CommitRepository commitRepository;
//
//    @Autowired
//    private ChangedPartByCommit changedPartByCommit;
//
//    public void fetchAndSaveCommits(Long memberId) {
//        GitHubAuth token = gitHubAuthService.getTokenByMemberId(memberId);
//        String accessToken = token.getAccessToken();
//
//        List<Repository> repos = repositoryRepository.findByMemberId(memberId);
//
//        for (Repository repo : repos) {
//            String owner = repo.getOwner();
//            String repoName = repo.getTitle();
//
//            List<Map<String, Object>> commits = gitHubService.getCommits(owner, repoName, accessToken);
//
//            for (Map<String, Object> commit : commits) {
//                String sha = (String) commit.get("sha");
//                String message = (String) ((Map<String, Object>) commit.get("commit")).get("message");
//
//                // 중복 방지용 체크
//                if (!commitRepository.existsByCommitHash(sha)) {
//                    Commit newCommit = Commit.builder()
//                            .memberId(memberId)
//                            .repositoryId(repo.getId())
//                            .message(message)
//                            .commitHash(sha)
//                            .diffSummary(0L) // 이후 분석값 넣을 수 있음
//                            .qualityScore(0L)
//                            .regDate(LocalDateTime.now())
//                            .build();
//
//                    commitRepository.save(newCommit);
//
//                    // 🔍 변경 파일들까지 조회
//                    Map<String, Object> detail = gitHubService.getCommitDetail(owner, repoName, sha, accessToken);
//                    List<Map<String, Object>> files = (List<Map<String, Object>>) detail.get("files");
//
//                    for (Map<String, Object> file : files) {
//                        String filePath = (String) file.get("filename");
//                        String patch = (String) file.get("patch"); // diff
//                        String status = (String) file.get("status"); // added, removed, modified
//
//                        ChangedPartByCommit changed = ChangedPartByCommit.builder()
//                                .commitId(newCommit.getId())
//                                .filePath(filePath)
//                                .changeType(status) // enum값 그대로 저장
//                                .codeDiff(patch != null ? patch : "")
//                                .summary("") // 나중에 GPT가 요약
//                                .functionName("") // 추출 로직 추후 추가
//                                .build();
//
//                        changedPartByCommit.save(changed);
//                    }
//                }
//            }
//        }
//    }
//}
