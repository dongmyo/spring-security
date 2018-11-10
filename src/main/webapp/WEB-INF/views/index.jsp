<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head lang="ko">
    <meta charset="UTF-8">
    <title>Main</title>
</head>
<body>
    <h1>Main</h1>

    <jsp:include page="fragments/heading.jsp" />

    <ul>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <li>
                <a href="/admin/manage">Admin Tool</a>
            </li>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
            <li>
                <a href="/project/1">Public Project</a>
            </li>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('ROLE_ADMIN','ROLE_MEMBER')">
            <li>
                <a href="/private-project/2">Private Project</a>
            </li>
        </sec:authorize>
    </ul>

    <br />

</body>
</html>
