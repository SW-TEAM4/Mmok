<%@ page import="com.example.mmok.Game.GameVo" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="OmokPage" method="get">
</form>


<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>게임 방</title>
</head>
<body>
<!-- 회원이 아닐 경우 메시지 출력 -->
<c:if test="${not empty errorMessage}">
  <p style="color:red;">${errorMessage}</p>
</c:if>

<!-- 게임방 생성 -->
<c:if test="${not empty message}">
  <h1>${message}</h1>
  <!-- 게임 코드 출력 -->
  <p>게임 코드: ${gameCode}</p>
</c:if>

<!-- 게임 방에 입장한 경우 -->
<c:if test="${not empty gameCode and empty message}">
  <h1>게임 방에 입장하셨습니다!</h1>

  <p>게임 코드: ${gameCode}</p>
</c:if>

</body>
</html>