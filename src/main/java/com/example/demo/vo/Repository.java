package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repository {
    private Long id;
    private Long memberId;
    private Long githubId;
    private String title;
    private String url;
    private Long lastRequestCommitId;
    private Boolean delStatus;
    private LocalDateTime delDate;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
