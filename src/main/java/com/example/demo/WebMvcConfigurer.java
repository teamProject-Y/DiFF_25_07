
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.example.demo.interceptor.BeforeActionInterceptor;
import com.example.demo.interceptor.NeedLoginInterceptor;
import com.example.demo.interceptor.NeedLogoutInterceptor;

@Configuration
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    private final DemoApplication demoApplication;

	// BeforeActionInterceptor 연결
	@Autowired
	BeforeActionInterceptor beforeActionInterceptor;

	// NeedLoginInterceptor 연결
	@Autowired
	NeedLoginInterceptor needLoginInterceptor;

	// NeedLogoutInterceptor 연결
	@Autowired
	NeedLogoutInterceptor needLogoutInterceptor;

    WebMvcConfigurer(DemoApplication demoApplication) {
        this.demoApplication = demoApplication;
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
			
		// 모든 요청이 들어오기 전에 before 인터셉터 활용하겠다
		registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**");
		
		registry.addInterceptor(needLoginInterceptor).addPathPatterns("/usr/article/write")
				.addPathPatterns("/usr/article/doWrite").addPathPatterns("/usr/article/modify")
				.addPathPatterns("/usr/article/doModify").addPathPatterns("/usr/article/doDelete")
				.addPathPatterns("/usr/member/doLogout").addPathPatterns("/usr/article/doCommentWrite")
				.addPathPatterns("/usr/member/modify").addPathPatterns("/usr/member/myInfo")
				.addPathPatterns("/usr/member/doModify");
		
		registry.addInterceptor(needLogoutInterceptor).addPathPatterns("/usr/member/login")
		.addPathPatterns("/usr/member/doLogin").addPathPatterns("/usr/member/join")
		.addPathPatterns("/usr/member/doJoin");
		
	}
}