# AM_spring_25_05 데이터 베이스 추가
DROP DATABASE IF EXISTS AM_spring_25_05;
CREATE DATABASE AM_spring_25_05;
USE AM_spring_25_05;


# 게시물 테이블 추가
CREATE TABLE article(
    id INT(100) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title VARCHAR(100) NOT NULL,
    `body` TEXT NOT NULL,
    memberId INT(100) UNSIGNED NOT NULL,
    boardId INT(10) UNSIGNED NOT NULL,
    hits INT(100) UNSIGNED NOT NULL DEFAULT 0
);

# 게시물 테이블 데이터 추가
INSERT INTO article
SET regDate = NOW(),
    updateDate = NOW(),
    title = CONCAT('제목', SUBSTRING(RAND() *1000 FROM 1 FOR 2)),
    `body` = CONCAT('내용', SUBSTRING(RAND() *1000 FROM 1 FOR 2)),
    memberId = FLOOR(1 + RAND() * 5),
    boardId = FLOOR(1 + RAND() * 3);



# 회원 테이블 추가
CREATE TABLE `member`(
    id INT(100) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    loginId VARCHAR(100) NOT NULL UNIQUE,
    loginPw VARCHAR(100) NOT NULL,
    `authLevel` SMALLINT(2) UNSIGNED DEFAULT 3 COMMENT '권한 레벨 (3=일반/7=관리자)',
    `name` VARCHAR(10) NOT NULL,
    nickName VARCHAR(10) NOT NULL,
    cellPhone VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '탈퇴 여부(0=존재/1=탈퇴)',
    delDate DATETIME COMMENT '탈퇴날짜'
);

# 회원 테이블 수정
ALTER TABLE `member` ADD COLUMN updateDate DATETIME AFTER regDate;
UPDATE MEMBER
SET updateDate = regDate;

# admin 회원 데이터 추가
INSERT INTO `member`
SET regDate = NOW(),
    loginId = 'admin',
    loginPw = 'admin',
    `authLevel` = 7,
    `name` = 'admin',
    nickName = 'admin',
    cellPhone = '01012345678',
    email = 'admin@spring.com'; 

# 회원 테이블 데이터 추가
INSERT INTO `member`
SET regDate = NOW(),
    loginId = CONCAT('id', SUBSTRING(RAND() *1000 FROM 1 FOR 2)),
    loginPw = CONCAT('pw', SUBSTRING(RAND() *1000 FROM 1 FOR 2)),
    `name` = CONCAT('이름', SUBSTRING(RAND() *1000 FROM 1 FOR 2)),
    nickName = CONCAT('닉네임', SUBSTRING(RAND() *1000 FROM 1 FOR 2)),
    cellPhone = CONCAT('010123456', SUBSTRING(RAND() *1000 FROM 1 FOR 2)),
    email = CONCAT(SUBSTRING(RAND() *1000 FROM 1 FOR 2), '@gmail.com'); 
    
    
    
# board 테이블 추가		
CREATE TABLE board(
	id INT(100) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
	`code` VARCHAR(100) NOT NULL UNIQUE COMMENT 'Notice(공지사항), Free(자유게시판) QnA(질의응답)',    
    `name` VARCHAR(100) NOT NULL UNIQUE COMMENT '게시판 이름',
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '삭제 여부(0=존재/1=삭제)',
    delDate DATETIME COMMENT '삭제일'
);

# board 테이블 데이터 추가
INSERT INTO board SET 
	regDate = NOW(),
	updateDate = NOW(),
	`code` = 'notice',
	`name` = '공지사항';
	
INSERT INTO board SET 
	regDate = NOW(),
	updateDate = NOW(),
	`code` = 'free',
	`name` = '자유게시판';

INSERT INTO board SET 
	regDate = NOW(),
	updateDate = NOW(),
	`code` = 'QnA',
	`name` = '질의응답';
	
	

# like 테이블 추가
--CREATE TABLE `like` (
--	id INT(100) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
--	memberId INT(100) UNSIGNED NOT NULL,
--	articleId INT(100) UNSIGNED NOT NULL
--);

# like 테이블 데이터 추가
--INSERT INTO `like` SET 
--	memberId = FLOOR(1 + RAND() * 4), 
--	articleId = FLOOR(1 + RAND() * 5);
	
	
	
# reactionPoint 테이블 추가
CREATE TABLE reactionPoint (
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	memberId INT(100) UNSIGNED NOT NULL,
	relTypeCode CHAR(50) NOT NULL COMMENT '관련 데이터 코드',
	relId INT(10) NOT NULL COMMENT '관련 데이터 id',
	`point` INT(10) NOT NULL
);

# reactionPoint 테이블 제약조건 추가
ALTER TABLE reactionPoint
ADD UNIQUE KEY uniq_member_type_rel (memberId, relTypeCode, relId);

# reactionPoint 테이블 중복 제거
DELETE rp1
FROM reactionPoint rp1
JOIN reactionPoint rp2
  ON rp1.memberId = rp2.memberId
 AND rp1.relId = rp2.relId
 AND rp1.id > rp2.id;

# reactionPoint 테이블 데이터 추가
INSERT INTO reactionPoint SET
	regDate = NOW(), 
	updateDate = NOW(),
	memberId = FLOOR(1 + RAND() * 8),
	relTypeCode = 'comment',
	relId = FLOOR(1 + RAND() * 104),
	`point` = (2 * FLOOR(RAND() * 2)) - 1;



# 댓글 테이블 추가
CREATE TABLE `comment` (
	id INT(100) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	memberId INT(100) UNSIGNED NOT NULL,
	relTypeCode CHAR(50) NOT NULL COMMENT '관련 데이터 타입 코드',
	relId INT(100) UNSIGNED NOT NULL COMMENT '관련 데이터 id',
	`body` TEXT NOT NULL
);

# 댓글 테이블 수정
ALTER TABLE `comment` CHANGE articleId relId INT(100) UNSIGNED NOT NULL COMMENT('관련 데이터 id');
ALTER TABLE `comment` ADD COLUMN relTypeCode CHAR(50) NOT NULL AFTER memberId COMMENT '관련 데이터 타입 코드';
UPDATE `comment` SET relTypeCode = 'article';

# 댓글 테이블 데이터 추가
INSERT INTO `COMMENT` SET
	regDate = NOW(),
	updateDate = NOW(),
	memberId = FLOOR(1 + RAND() * 4),
	relTypeCode = 'article',
	relId = FLOOR(1 + RAND() * 64),
	`body` = 'comment';

# faq 테이블 추가	
CREATE TABLE faq (
    id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    `body` TEXT NOT NULL,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL
);

# faq 테이블 데이터 추가
INSERT INTO faq SET
    title = "특정 단어를 검색하는 법은 무엇인가요?",
    `body` = "특정 단어를 검색하는 법은 게시판 메뉴에 접속하여 검색하고 싶은 게시판, 검색 대상을 선택하신 후 
    상단의 검색어 입력창에 특정 단어를 입력하면 해당 단어가 포함된 게시물을 검색할 수 있습니다.",
    regDate = NOW(),
    updateDate = NOW();
    
INSERT INTO faq SET
    title = "게시물을 작성하는 법은 무엇인가요?",
    `body` = "게시물을 작성하기 위해서는 로그인이 필요합니다. 로그인을 하신 후 게시판 메뉴에 접속하여 
    화면 상단의 '글 작성' 버튼을 클릭하신 후 게시판 선택과 글 작성이 가능합니다. 
    게시판 선택은 필수이며, 적절한 게시판을 선택하여 주시길 바랍니다.
    또, 게시글을 작성하실 때는 제목과 내용이 공란이거나 공백으로만 이루어진 경우 글 작성이 불가합니다.",
    regDate = NOW(),
    updateDate = NOW();

INSERT INTO faq SET
    title = "게시물을 수정하는 법은 무엇인가요?",
    `body` = "게시글은 작성자 본인만 수정이 가능합니다. 
    로그인 후 수정하고 싶은 게시물 페이지에 접속하면 제목 우측에 'Modify' 버튼을 클릭하시어 해당 게시글을 수정할 수 있습니다.
    게시글 수정 시 제목과 내용이 공란이거나 공백으로만 이루어진 경우 글 수정이 불가합니다.",
    regDate = NOW(),
    updateDate = NOW();
    
INSERT INTO faq SET
    title = "게시물을 삭제하는 법은 무엇인가요?",
    `body` = "게시글은 작성자 본인만 삭제가 가능합니다. 
    로그인 후 삭제하고 싶은 게시물 페이지에 접속하면 제목 우측에 'Delete' 버튼을 클릭하시어 해당 게시글을 삭제 할 수 있습니다.
    게시글 삭제 후 취소는 불가하오니 이 점 유의해 주시길 바랍니다.",
    regDate = NOW(),
    updateDate = NOW();


SELECT * FROM article;
SELECT * FROM `member`;
SELECT * FROM board;
SELECT * FROM `like`;
SELECT * FROM `comment`;
SELECT * FROM reactionPoint;