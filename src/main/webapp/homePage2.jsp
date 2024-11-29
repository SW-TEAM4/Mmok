<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>홈페이지</title>
</head>
<body>
<h1>게임 방 생성</h1>

<%-- 첫번째 유저 접속--%>
<form action="OmokPage" method="post">
  <input type="hidden" name="action" value="createRoom" />
  <label for="userId1">유저1 닉네임:</label>
  <input type="text" id="userId1" name="userId1" required />
  <br>
  <input type="submit" />
</form>

<c:if test="${not empty errorMessage1}">
  <p style="color:red;">${errorMessage1}</p>
</c:if>

<h1>게임 방 입장</h1>

<!-- 두 번째 유저가 게임방에 입장하는 폼 -->
<form action="OmokPage" method="post">
  <input type="hidden" name="action" value="joinRoom" />
  <label for="userId2">유저2 닉네임:</label>
  <input type="text" id="userId2" name="userId2" required />
  <br>
  <label for="gameCode">게임 코드:</label>
  <input type="text" id="gameCode" name="gameCode" required />
  <br>
  <input type="submit" value="게임 방 입장" />
</form>

<!-- 회원이 아닐 경우 메시지 출력 -->
<c:if test="${not empty errorMessage2}">
  <p style="color:red;">${errorMessage2}</p>
</c:if>

<c:if test="${not empty errorMessage3}">
  <p style="color:red;">${errorMessage3}</p>
</c:if>


</body>
</html>