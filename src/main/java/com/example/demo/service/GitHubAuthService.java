package com.example.demo.service;

import com.example.demo.repository.GitHubAuthRepository;
import com.example.demo.vo.GitHubAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubAuthService {

    @Autowired
    private GitHubAuthRepository gitHubAuthRepository;

    public void saveGitHubToken(Long memberId, String accessToken, String tokenType, String scope) {
        GitHubAuth auth = GitHubAuth.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .tokenType(tokenType)
                .scope(scope)
                .build();

        gitHubAuthRepository.save(auth);
    }

    public GitHubAuth getLatestTokenByMemberId(int memberId) {
        return gitHubAuthRepository.findLatestByMemberId(memberId);
    }

}
