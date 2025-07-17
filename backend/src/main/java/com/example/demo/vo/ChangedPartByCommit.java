package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangedPartByCommit {
    private Long id;
    private Long commitId;
    private String filePath;
    private ChangeType changeType;
    private String codeDiff;
    private String summary;
    private String functionName;

    // enum 정의
    public enum ChangeType {
        add, update, delete
    }
}
