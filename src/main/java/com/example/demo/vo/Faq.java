package com.example.demo.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Faq {
	
	public Faq(String title, String body) {
		this.title = title;
		this.body = body;
	}

	private int id;
	private String title;
	private String body;
	private LocalDateTime regDate;
	private LocalDateTime updateDate;
}
