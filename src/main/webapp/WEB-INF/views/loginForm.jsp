<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head lang="ko">
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>

<h1>Login</h1>
<br />
<br />

<!-- TODO : #3 로그인 에러 메세지 출력 ログインエラーメッセージ出力 -->
<c:if test="${param.error != null}">
    <div style="color: red; ">ID or password is invalid</div>
    <br />
</c:if>

<form method="post" action="/login/process">
    ID : <input type="text" name="name" value="" /><c:if test="${invalidUsername == true}"> <span style="color: red; ">Not Found ID
    </span></c:if><br />
    PW : <input type="password" name="pwd" value="" /><c:if test="${invalidPassword == true}"> <span style="color: red; ">ID and password do not match.
    </span></c:if><br />
    <br />

    <input type="submit" value="Login!" />
</form>
<br />
</body>
</html>
