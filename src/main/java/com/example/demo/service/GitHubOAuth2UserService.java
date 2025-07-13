package com.example.demo.service;

import com.example.demo.repository.RepositoryRepository;
import com.example.demo.vo.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.vo.Repository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GitHubOAuth2UserService extends DefaultOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private MemberService memberService;

    @Autowired
    private GitHubAuthService gitHubAuthService;

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RepositoryRepository repositoryRepository;
    @Autowired
    private HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String oauthId = oauthUser.getName();

        String username = null;
        String email = null;

        if ("github".equals(registrationId)) {
            username = oauthUser.getAttribute("login");
            email = fetchPrimaryEmail(userRequest);
        } else if ("google".equals(registrationId)) {
            username = oauthUser.getAttribute("name");
            email = oauthUser.getAttribute("email");
        }

        memberService.processOAuthPostLogin(oauthId, username, email);

        Member member = memberService.getByOauthId(oauthId);
        if (member != null) {
            session.setAttribute("loginedMemberId", member.getId());
        }

        if ("github".equals(registrationId)) {
            String accessToken = userRequest.getAccessToken().getTokenValue();
            String tokenType = userRequest.getAccessToken().getTokenType().getValue();
            String scope = String.join(",", userRequest.getAccessToken().getScopes());

            gitHubAuthService.saveGitHubToken((long) member.getId(), accessToken, tokenType, scope);
            saveGitHubRepos(accessToken, (long) member.getId());
        } else {
            System.out.println("Google 로그인 - GitHub 관련 처리 생략");
        }

        return oauthUser;
    }


    private String fetchPrimaryEmail(OAuth2UserRequest userRequest) {
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String emailApiUrl = "https://api.github.com/user/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                emailApiUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );


        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> emails = response.getBody();
            System.out.println("📨 이메일 리스트: " + emails);

            for (Map<String, Object> emailEntry : emails) {
                Boolean primary = (Boolean) emailEntry.get("primary");
                Boolean verified = (Boolean) emailEntry.get("verified");
                String email = (String) emailEntry.get("email");


                if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
                    System.out.println("✅ primary & verified 이메일 선택됨: " + email);
                    return email;
                }
            }
        }

        return null;
    }

    private void saveGitHubRepos(String accessToken, Long memberId) {
        List<Map> repoMapList = gitHubService.fetchGitHubRepos(accessToken);

        List<Repository> repos = repositoryService.convertGitHubRepoMapToEntity(repoMapList, memberId);

        for (Repository repo : repos) {
            Repository existing = repositoryRepository.findByGithubIdAndMemberId(repo.getGithubId(), memberId);
            if (existing == null) {
                repositoryRepository.save(repo);
            } else {
                System.out.println("이미 존재하는 리포지토리: " + repo.getTitle());
            }
        }
    }
}