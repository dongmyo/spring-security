<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head lang="ko">
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<!-- TODO : #3 Login Form -->

<h1>Login</h1>
<br />
<br />

<form method="post" action="/login/process">
    ID : <input type="text" name="name" value="" /><br />
    PW : <input type="password" name="pwd" value="" /><br />
    <br />

    <input type="submit" value="Login!" />
</form>
<br />
</body>
</html>
