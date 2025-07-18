package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

	public LoginDto(String loginId, String loginPw, String name, String nickName, String cellPhone, String email) {
		this.loginId = loginId;
		this.loginPw = loginPw;
		this.name = name;
		this.nickName = nickName;
		this.cellPhone = cellPhone;
		this.email = email;
	}

	private int id;
	private String oauthId;
	private LocalDateTime regDate;
	private String loginId;
	private String loginPw;
	private String name;
	private String nickName;
	private String cellPhone;
	private String email;
	private boolean delStatus;
	private LocalDateTime delDate;
}