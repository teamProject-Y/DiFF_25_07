<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="MAIN PAGE"></c:set>
<%@ include file="../common/head.jspf"%>


<div class="container w-2/3 mx-auto mt-20 border border-neutral-300 rounded-xl p-3">

	<div class="text-4xl text-bold m-6">자주 묻는 질문 검색</div>

	<form action="faq" class="flex mx-8 my-10 px-4">	
		<input name="question" type="text" placeholder="궁금한 점을 입력하세요." value="${not empty question ? question : ''}"
		class="flex-grow mr-2 border border-neutral-300 p-4 rounded-xl"/>
		<button type="submit" class="px-6 border border-neutral-300 rounded-xl">검색</button>
	</form>
	
	<div class="mx-6 p-6 border border-neutral-300 rounded-xl">
		<c:if test="${not empty faq }">
			<div>질문: ${faq.title }</div>
			<hr class="my-4" />
			<div>답변: ${faq.body }</div>
		</c:if>
	</div>
</div>

<%@ include file="../common/foot.jspf"%>