package com.example.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {


	private int id;
	private LocalDateTime regDate;
	private String loginId;
	private String oauthId;
	private String loginPw;
	private String name;
	private String nickName;
	private String cellPhone;
	private String email;
	private boolean delStatus;
	private LocalDateTime delDate;
}