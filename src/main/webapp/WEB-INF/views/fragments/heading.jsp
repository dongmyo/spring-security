<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAuthenticated()">
    <!-- TODO : #7 spring security taglib를 이용해서 authentication 객체의 principal.username을 출력하세요. -->
    ???님,
    <a href="<c:url value='/logout' />">로그아웃</a>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <a href="/redirect-index">로그인</a>
</sec:authorize>
<br />
<a href="/">메인으로</a>
<br />
<br />
<br />