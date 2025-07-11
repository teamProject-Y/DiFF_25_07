package com.example.demo.vo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubAuth {
    private Long id;
    private Long memberId;
    private String accessToken;
    private String tokenType;
    private String scope;
    private String fetchedAt;
}
