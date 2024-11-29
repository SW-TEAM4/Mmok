<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 가입</title>
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
            width: 340px;
        }

        .form-group {
            display: flex;
            align-items: center;
            margin-bottom: 12px; 
        }

        .form-group label {
            flex: 1;
            font-size: 14px;
            text-align: left;
            margin-right: 8px;
        }

        .form-group input[type="text"],
        .form-group input[type="password"] {
            flex: 2;
            padding: 8px;
            border: none;
            border-bottom: 2px solid #fff;
            background-color: #FFFFFF;
            color: black;
            outline: none;
        }

        .form-group-row {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .form-group-row input[type="text"] {
            flex: 3;
            padding: 10px;
            border: none;
            border-bottom: 2px solid #fff;
            background-color: #FFFFFF;
            color: black;
            outline: none;
        }

        .form-group-row .btn-check {
            flex: 1;
            background-color: #d8f716;
            color: black;
            border: none;
            border-radius: 10px;
            padding: 8px;
            font-size: 12px;
            cursor: pointer;
        }

        .form-container .btn-submit {
            width: 100%;
            background-color: #d8f716;
            color: black;
            border: none;
            border-radius: 10px;
            padding: 10px;
            font-size: 14px;
            cursor: pointer;
            margin-top: 10px;
        }
        .error {
            color: red;
            font-size: 14px;
            margin-bottom: 12px;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <form action="register" method="post">
            <div class="error">
                <c:if test="${not empty errorMessage}">
                    ${errorMessage}
                </c:if>
            </div>

            <div class="form-group">
                <label for="id">ID</label>
                <input type="text" id="id" name="user_id" required>
            </div>

            <div class="form-group">
                <label for="pwd">PWD</label>
                <input type="password" id="pwd" name="user_pwd" required>
            </div>

            <div class="form-group">
                <label for="nickname">NICKNAME</label>
                <input type="text" id="nickname" name="user_name" required>
            </div>

            <button type="submit" class="btn-submit">가입하기</button>
        </form>
    </div>
</body>
</html>