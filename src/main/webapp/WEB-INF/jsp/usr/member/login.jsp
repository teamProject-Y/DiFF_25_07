<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="MEMBER LOGIN"></c:set>
<%@ include file="../common/head.jspf"%>


<div class="container mx-auto mt-32 max-w-min p-4 bg-neutral-200 border border-solid border-neutral-300 rounded-lg">
		<div class="title mt-4 mb-8 text-center text-2xl font-semibold">
			Login
		</div>
		<form name="login" action="/usr/member/doLogin" method="POST">

			<!-- 로그인 실패 메시지 -->
			<c:if test="${not empty param.error}">
				<div class="text-red-500 text-center mb-4">❌ 로그인 실패: 아이디 또는 비밀번호를 확인하세요.</div>
			</c:if>

			<div style="display:flex; flex-direction:column; justify-content: center;">
	  			
				<input type="text" name="loginId" class="mb-6 bg-neutral-50 border border-neutral-300 text-neutral-800 text-sm rounded-lg block w-96 p-2.5" placeholder="ID">
				<input type="text" name="loginPw" class="mb-6 bg-neutral-50 border border-neutral-300 text-neutral-800 text-sm rounded-lg block w-96  p-2.5" placeholder="Password">

	 		</div>
	 		<button type="submit" class="py-2.5 px-5 me-2 mb-2 w-96 text-sm font-large bg-neutral-800 text-neutral-200 rounded-lg hover:bg-neutral-700">Login</button>
		</form>
		<div class="sub-menu text-center my-4 flex justify-center">
			<a class="hover:text-underline" href="join">Join</a>
<!-- 			<a class="hover:text-underline"  href="#">Find ID</a> -->
<!-- 			<a class="hover:text-underline"  href="#">Find Password</a>		 -->
		</div>


	</div>
<!-- 👇 소셜 로그인 버튼들 추가 -->
<div class="mt-6">
    <a href="/oauth2/authorization/github"
       class="flex items-center justify-center gap-3 bg-black text-white py-2 px-4 rounded hover:bg-gray-800 transition">
        <img src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"
             alt="GitHub Logo" class="w-6 h-6">
        <span>GitHub로 로그인</span>
    </a>

    <a href="/oauth2/authorization/google"
       class="bg-red-500 text-white text-center py-2 rounded block hover:bg-red-600 mt-4">
        <img src="https://developers.google.com/identity/images/g-logo.png"
             alt="Google Logo" class="inline w-5 h-5 mr-2">
        Google로 로그인
    </a>
</div>

<%@ include file="../common/foot.jspf"%>