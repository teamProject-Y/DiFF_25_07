create database project_Y;
use project_Y;

# 회원
create table `member` (
                          `id` bigint not null auto_increment primary key,
                          `loginId` varchar(20) not null,
                          `loginPw` varchar(20) not null,
                          `name` varchar(100) not null,
                          `birthDate` date not null,
                          `email` varchar(255) not null,
                          `nickName` varchar(30) not null,
                          `oAuthId` varchar(255),
                          `regDate` datetime not null,
                          `updateDate` datetime not null
);

# 팔로우
create table follow (
                        id bigint not null auto_increment primary key,
                        toMemberId bigint not null,
                        fromMemberId bigint not null,
                        regDate datetime not null,
                        updateDate datetime not null
);

# 리포지토리
create table repository (
                            id bigint not null auto_increment primary key,
                            memberId bigint not null,
                            githubId bigint not null,
                            title varchar(255) not null,
                            url varchar(255) not null,
                            lastRequestCommitId bigint not null,
                            delStatus boolean not null default 0,
                            delDate datetime,
                            regDate datetime not null,
                            updateDate datetime not null,
                            owner varchar(20) not null #추가해야댐
);

# 커밋
create table commit (
                        id bigint not null auto_increment primary key,
                        memberId bigint not null,
                        repositoryId bigint not null,
                        message text not null, # not null???
    diffSummary bigint not null, # diffSummary??
    qualityScore bigint not null, # bigint???
    commitHash varchar(255) not null,
                        regDate datetime not null,
                        requestDate datetime
);

# 커밋 내 변경 부분
create table changedPartByCommit (
                                     id bigint not null auto_increment primary key,
                                     commitId bigint not null,
                                     filePath varchar(255) not null,
                                     changeType enum('add', 'update', 'delete') not null, # ???
    codeDiff longText not null,
                                     summary text not null,
                                     functionName varchar(255) not null
);

# 커밋 내 변경 부분의 변경 키워드
create table changedKeywordByPart (
                                      id bigint not null auto_increment primary key,
                                      changedCommitId bigint not null,
                                      keyword varchar(255) not null,
                                      regDate datetime not null,
                                      updateDate datetime not null ## updateDate??
);

# 게시물
create table article (
                         id bigint not null auto_increment primary key,
                         memberId bigint not null,
                         title text not null,
                         `body` longtext not null,
                         hits bigint not null default 0,
                         regDate datetime not null,
                         updateDate datetime not null
);

# 댓글
create table reply (
                       id bigint not null auto_increment primary key,
                       memberId bigint not null,
                       articleId bigint not null,
                       `body` longtext not null,
                       regDate datetime not null,
                       updateDate datetime not null
);


# 좋아요
create table reaction (
                          id bigint not null auto_increment primary key,
                          memberId bigint not null,
                          articleId bigint not null,
                          regDate datetime not null,
                          updateDate datetime not null
);

#git API 호출 테이블
CREATE TABLE githubAuth (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            memberId INT NOT NULL,
                            accessToken TEXT NOT NULL,
                            tokenType VARCHAR(20),
                            scope VARCHAR(255),
                            fetchedAt DATETIME DEFAULT NOW()
);