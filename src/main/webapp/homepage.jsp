<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: 이윤채
  Date: 2024-11-25
  Time: 오후 5:35
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>HomePage</title>
  <style>
    body {
      margin: 0;
      padding: 20px;
      font-family: Arial, sans-serif;
      background-color: #212121;
    }
    .container {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 30px;
      background-color: #212121;
    }
    .row {
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;
      gap: 40px;
      width: 100%;
      background-color: #212121;
    }
    .box, .ranking {
      height: 200px;
      background-color:#33FF33
    }
    .box {
      border: 1px solid #000;
      border-radius: 20px;
      padding: 20px;
      width: 200px;
      text-align: center;
      font-size: 20px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

    }
    .user-box {
      display: flex;
      flex-direction: row;
      align-items: center;
      border: 1px solid #000;
      border-radius: 20px;
      width: 400px;
      padding: 20px;
      gap: 20px;
      height: 200px;
      background-color:#33FF33
    }
    .user-box img {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      border: 1px solid #ccc;
    }
    .user-info {
      display: flex;
      flex-direction: column;
      justify-content: center;
    }
    .user-info h3 {
      margin: 0;
      font-size: 30px;
    }
    .user-info p {
      margin: 0;
      font-size: 20px;
    }
    .ranking {

      width: 400px;
      border: 1px solid #000;
      border-radius: 20px;
      padding: 20px;
      text-align: center;
      display: flex;
      flex-direction: column;
      justify-content: center;
    }
    .ranking table {
      width: 100%;
      border-collapse: collapse;
    }
    .ranking th, .ranking td {
      border: 1px solid #000;
      padding: 10px;
      text-align: center;
    }
    .ranking th {
      background-color:#33FF33
    }
    input[type="text"] {
      width: 100%;
      border: none;
      outline: none;
      text-align: center;
      font-size: 18px;
      background-color:#33FF33;
    }
    button {
      margin-top: 10px;
      padding: 5px 10px;
      background-color:#33FF33;
      border: 1px solid #000;
      border-radius: 5px;
      cursor: pointer;
      font-size: 16px;
    }
    button:hover {
      background-color: #F7DC6F;
    }
  </style>
</head>
<body>
<div class="container">
  <div class="row">
    <div class="box">방 만들기</div>
    <div class="user-box">
      <img src="img/rangking_header_img.png" alt="User Icon">
      <div class="user-info">
        <h3>user_nickname</h3>
        <p>승률: 4승 1패</p>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="box">
      <form method="POST" action="/home">
        <input
                type="text"
                name="inputValue"
                placeholder="코드를 입력하세요"
        />
        <button type="submit">확인</button>
      </form>
    </div>
    <div class="ranking">
      <h3>오늘의 순위</h3>
      <table>
        <tr>
          <th>순위</th>
          <th>닉네임</th>
          <th>전적</th>
        </tr>
        <%
          List<String[]> rankings = (List<String[]>) request.getAttribute("rankings");
          if (rankings != null) {
            for (String[] rank : rankings) {
        %>
        <tr>
          <td><%= rank[0] %></td>
          <td><%= rank[1] %></td>
          <td><%= rank[2] %></td>
        </tr>
        <%
            }
          }
        %>
      </table>
    </div>
  </div>
</div>
</body>
</html>