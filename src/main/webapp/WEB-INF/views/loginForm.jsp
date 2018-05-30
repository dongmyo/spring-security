<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head lang="ko">
    <meta charset="UTF-8">
    <title>로그인</title>
</head>
<body>

<h1>로그인</h1>
<br />
<br />

<c:if test="${param.error != null}">
    <div style="color: red; ">등록되지 않은 아이디이거나 아이디 또는 비밀번호가 일치하지 않습니다.</div>
    <br />
</c:if>

<form method="post" action="/login/process">
    이름 : <input type="text" name="name" value="" /><c:if test="${invalidUsername == true}"> <span style="color: red; ">등록되지 않은 아이디입니다
    .</span></c:if><br />
    비번 : <input type="password" name="pwd" value="" /><c:if test="${invalidPassword == true}"> <span style="color: red; ">비밀번호가 일치하지 않습니다
    .</span></c:if><br />
    <br />

    <input type="submit" value="로그인!" />
</form>
<br />
</body>
</html>
