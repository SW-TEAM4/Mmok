<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <style>
        body {
            background-color: black;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
        }

        .form-container {
            background-color: black;
            color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 320px;
            text-align: center;
        }

        .form-container img {
            width: 320px;
            margin-bottom: 20px;
        }

        .form-group {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 15px;
        }

        .form-group label {
            flex: 1;
            font-size: 14px;
            text-align: left;
        }

        .form-group input[type="text"],
        .form-group input[type="password"] {
            flex: 2;
            padding: 10px;
            border: none;
            border-bottom: 2px solid #fff;
            background-color: #fff;
            color: black;
            outline: none;
        }

        .btn-login, .btn-signup {
            display: block;
            width: 100%;
            padding: 12px;
            margin-top: 10px;
            border: none;
            border-radius: 10px;
            font-size: 14px;
            cursor: pointer;
        }

        .btn-login {
            background-color: #d8f716;
            color: black;
        }

        .btn-signup {
            background-color: black;
            color: #d8f716;
            text-decoration: none;
            display: block;
            text-align: left;
            padding-left: 140px;
        }

        .btn-signup:hover {
            color: #a1c900;
        }

        .error {
            color: red;
            font-size: 14px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <img src="img/login.svg" alt="로그인 이미지">

        <form action="login" method="post">
            <div class="error">
                <c:if test="${not empty errorMessage}">
                    ${errorMessage}
                </c:if>
            </div>

            <div class="form-group">
                <label for="id">ID</label>
                <input type="text" id="id" name="user_id"  required>
            </div>

            <div class="form-group">
                <label for="pwd">PWD</label>
                <input type="password" id="pwd" name="user_pwd"  required>
            </div>

            <button type="submit" class="btn-login">로그인</button>

            <a href="register.jsp" class="btn-signup">회원가입</a>
        </form>
    </div>
</body>
</html>