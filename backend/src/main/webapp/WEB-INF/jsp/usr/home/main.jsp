<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="MAIN PAGE"></c:set>
<%@ include file="../common/head.jspf"%>


<div>
	메인입니다.
	<form action="/analyzeZip" method="post" enctype="multipart/form-data">
  <input type="file" name="zipFile" accept=".zip" required />
  <button type="submit">분석 요청</button>
</form>

</div>

<%@ include file="../common/foot.jspf"%>