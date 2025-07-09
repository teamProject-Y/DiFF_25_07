<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="MEMBER LOGIN"></c:set>
<%@ include file="../common/head.jspf"%>


<div class="container mx-auto mt-32 max-w-min p-4 bg-neutral-200 border border-solid border-neutral-300 rounded-lg">
		<div class="title mt-4 mb-8 text-center text-2xl font-semibold">
			Login
		</div>
		<form name="login" action="doLogin" method="POST">
			
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

<%@ include file="../common/foot.jspf"%>