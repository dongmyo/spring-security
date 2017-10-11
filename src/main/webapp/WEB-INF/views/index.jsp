<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head lang="ko">
    <meta charset="UTF-8">
    <title>메인</title>
</head>
<body>
    <h1>메인</h1>

    <jsp:include page="fragments/heading.jsp" />

    <ul>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <li>
                <a href="/admin/manage">관리툴</a>
            </li>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
            <li>
                <a href="/project/1">프로젝트</a>
            </li>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('ROLE_ADMIN','ROLE_MEMBER')">
            <li>
                <a href="/public-project/2">공개 프로젝트</a>
            </li>
        </sec:authorize>
    </ul>

    <br />

</body>
</html>
