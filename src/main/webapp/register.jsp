<%--
  Created by IntelliJ IDEA.
  User: 이윤채
  Date: 2024-11-25
  Time: 오후 5:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>MMOK 회원가입</title>
</head>
<body>
<h2>회원가입</h2>
<form action="register" method="post">
  아이디: <input type="text" name="user_id" required><br>
  비밀번호: <input type="password" name="user_pwd" required><br>
  닉네임: <input type="text" name="user_name" required><br>
  <button type="submit">회원가입</button>
</form>
</body>
</html>