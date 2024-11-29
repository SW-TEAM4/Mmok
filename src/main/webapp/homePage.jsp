<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.example.mmok.Rangking.RangkingDAO"%>
<%@ page import="com.example.mmok.Rangking.RangkingVO"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HomePage</title>
<style>
body {
	margin: 0;
	padding: 0;
	font-family: Arial, sans-serif;
	display: flex;
	width: 100%;
	flex-direction: row;
	height: 100vh; /* 전체 화면 높이 */
	background-color: black; /* 배경색 */
}

.logo {
	margin-top: 80px;
	width: 150px; /* 로고 크기 */
}

.container {
	flex-direction: row;
	align-items: flex-start;
	justify-content: center;
	gap: 40px;
	background-color: black;
	width: 100%;
	display: flex; /* 전체 화면 너비 */
}

.left-column {
	margin-top: 220px;
	margin-left: 10px;
	width: 50%; /* 화면의 왼쪽 절반 */
	display: flex;
	flex-direction: column;
	align-items: flex-start;
	gap: 20px;
	max-width: 30%;
	width: 50%;
	z-index:9;
}

.right-column {
	width: 50%; /* 화면 오른쪽 절반 차지 */
	height: 100vh; /* 화면 전체 높이 */
	display: flex;
	align-items: center; /* 세로 중앙 정렬 */
	justify-content: center; /* 가로 중앙 정렬 */
	background-color: black; /* 필요 시 배경색 설정 */
}

.box {
	height: 130px;
	border: 1px solid #000;
	border-radius: 10px;
	padding: 20px;
	width: 90%;
	text-align: center;
	font-size: 20px;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
}

.user-box {
	margin-top: 80px;
	height: 200px;
	display: flex;
	flex-direction: row;
	align-items: center;
	border: 1px solid #000;
	border-radius: 10px;
	width: 100%;
	padding: 20px;
	gap: 20px;
	background-color: black;
}

.user-box img {
	margin-bottom: 80px;
	margin-left: 10px;
	width: 288px;
	height: 288px;
	border-radius: 50%;
}

.user-info {
	margin-bottom: 80px;
	display: flex;
	flex-direction: column;
	justify-content: center;
	color: white;
}

.user-info h3 {
	margin: 0;
	font-size: 32px;
}

.user-info p {
	margin: 0;
	font-size: 26px;
}

.create-room {
	background-color: #D2FD51;
	color: #000;
	font-size: 20px;
	font-weight: bold;
	height: 67px;
	display: flex;
	align-items: center;
	justify-content: center;
	width: 65%;
}

.join-room {
	background-color: #D2FD51;
	display: flex;
	flex-direction: row;
	align-items: center;
	justify-content: center;
	gap: 10px;
	padding: 10px;
	height: 40px;
	width: 70%;
}

.join-room input {
	font-size: 16px;
	text-align: center;
	padding: 10px;
	width: 60%;
}

.join-room button {
	font-size: 16px;
	font-weight: bold;
	padding: 10px 20px;
	border: 2px solid #D2FD51;
	border-radius: 5px;
	background-color: #D2FD51;
	color: #000;
	cursor: pointer;
}

.podium-container {
	position: relative; /* 텍스트의 기준점을 잡기 위해 relative 설정 */
	width: 100%;
	height: 100%;
	display: flex;
	justify-content: center; /* 가로 중앙 정렬 */
	align-items: center; /* 세로 중앙 정렬 */
	margin-top: 200px;
}

.podium-text {
	position: absolute;
	color: white;
	font-size: 20px;
	background-color: rgba(0, 0, 0, 0.5);
	padding: 5px 10px;
	border-radius: 5px;
	text-align: center;
	box-sizing: border-box;
}

/* 1등 텍스트 (가운데 사람 머리 위) */
.podium-text.first {
	top: 20%; /* 위치 조정: 머리 위 */
	left: 50%; /* 중앙 정렬 */
	transform: translate(-50%, -50%); /* 가로, 세로 중앙 정렬 */
}

/* 2등 텍스트 (왼쪽 사람 머리 위) */
.podium-text.second {
	top: 35%; /* 머리 위 */
	left: 20%; /* 왼쪽 위치 */
	transform: translate(-50%, -50%);
}

/* 3등 텍스트 (오른쪽 사람 머리 위) */
.podium-text.third {
	top: 30%; /* 머리 위 */
	left: 60%; /* 오른쪽 위치 */
}

.podium_img {
	margin-top: 200px;
	width: 600px;
	height: 800px;
	z-index: 1;
}

.form-container {
	margin-top: 20px;
	display: flex;
	flex-direction: column;
	gap: 20px;
	display: flex; /* 각 그룹 사이 간격 */
}

input[type="text"]::placeholder {
	color: #999; /* 플레이스홀더 색상 */
}

.btn-create {
	flex-shrink: 0; /* 버튼 크기 고정 */
}

.btn-enter {
	padding: 10px 15px; /* 코드입장 버튼 크기 */
	flex-shrink: 0; /* 버튼 크기 고정 */
}

/* Form Group 스타일 */
.form-group {
	flex-direction: column; /* 세로 배치 */
	gap: 20px; /* 각 입력창과 버튼 사이 간격 */
	background-color: #D2FD51; /* 배경색 */
	padding: 30px; /* 내부 여백 */
	border-radius: 10px; /* 둥근 테두리 */
	align-items: flex-start; /* 왼쪽 정렬 */
	width: 100%;
	display: flex; /* 전체 너비 */
}

/* 레이블 스타일 */
label {
	font-size: 22px;
	font-weight: bold;
	color: black; /* 레이블 텍스트 색상 */
}

/* 입력창 스타일 */
input[type="text"] {
	width: 100%; /* 입력창 너비를 버튼과 동일하게 설정 */
	padding: 10px; /* 내부 여백 */
	font-size: 18px; /* 텍스트 크기 */
	border: 1px solid #ccc; /* 테두리 추가 */
	border-radius: 5px; /* 둥근 모서리 */
	background-color: white; /* 흰 배경 */
	box-sizing: border-box; /* 패딩 포함 크기 계산 */
	outline: none; /* 포커스 테두리 제거 */
	box-shadow: none; /* 그림자 제거 */
}

/* 버튼 스타일 */
button {
	width: 100%; /* 버튼 너비를 입력창과 동일하게 설정 */
	padding: 15px; /* 버튼 내부 여백 */
	font-size: 20px; /* 텍스트 크기 */
	font-weight: bold; /* 텍스트 굵기 */
	color: black; /* 텍스트 색상 */
	background-color: #E7FFA1; /* 버튼 배경색 */
	border: none; /* 테두리 제거 */
	border-radius: 5px; /* 둥근 모서리 */
	cursor: pointer; /* 포인터 커서 */
}

/* 버튼 호버 효과 */
button:hover {
	background-color: #c9e748; /* 호버 시 색상 변경 */
}
</style>
</head>
<body>
	<div class="container">
		<img src="img/logo.svg" alt="MMOK Logo" class="logo">

		<div class="left-column">

			<div class="user-box">
				<img src="img/user_profile1.svg" alt="User Icon">
				<div class="user-info">
					<h3>
						<c:out value="${sessionScope.user_name}" />님
					</h3>
					<p>
						<c:out value="${sessionScope.win}" />
						승
						<c:out value="${sessionScope.lose}" />
						패
					</p>
				</div>
			</div>

			<div class="form-container">
				<!-- 방 만들기 -->

				<div class="form-group">
						<form action="GameRoomPage" method="post">
							<button type="submit" class="btn-create" name="action" value="createRoom">방만들기</button>
						</form>
				</div>

				<!-- 코드 입장 -->
				<div class="form-group">
						<form action="GameRoomPage" method="post">
							<label for="code-hash">#</label> <input type="text" id="code-hash"
								name="hash" placeholder="Enter #">
							<button type="submit" class="btn-enter" name="action"
								value="joinRoom">코드입장</button>
						</form>
				</div>
				<c:if test="${not empty errorMessage2}">
					<script type="text/javascript">
						alert('<c:out value="${errorMessage2}" escapeXml="false"/>');
					</script>
				</c:if>
				<c:if test="${not empty errorMessage3}">
					<script type="text/javascript">
						alert('<c:out value="${errorMessage3}" escapeXml="false"/>');
					</script>
				</c:if>
			</div>


		</div>
		<div class="right-column">
			<div class="podium-container">
				<!-- 1등 -->
				<div class="podium-text first">
					<c:out value="${sessionScope.list[0].userName}" />
					님<br>
					<c:out value="${sessionScope.list[0].totalCount}" />
					전
					<c:out value="${sessionScope.list[0].winCount}" />
					승
					<c:out value="${sessionScope.list[0].loseCount}" />
					패 | 승률
					<c:out value="${sessionScope.list[0].ranking}" />

				</div>
				<!-- 2등 -->
				<div class="podium-text second">
					<c:out value="${sessionScope.list[1].userName}" />
					님<br>
					<c:out value="${sessionScope.list[1].totalCount}" />
					전
					<c:out value="${sessionScope.list[1].winCount}" />
					승
					<c:out value="${sessionScope.list[1].loseCount}" />
					패 | 승률
					<c:out value="${sessionScope.list[1].ranking}" />

				</div>
				<!-- 3등 -->
				<div class="podium-text third">
					<c:out value="${sessionScope.list[2].userName}" />
					님<br>
					<c:out value="${sessionScope.list[2].totalCount}" />
					전
					<c:out value="${sessionScope.list[2].winCount}" />
					승
					<c:out value="${sessionScope.list[2].loseCount}" />
					패 | 승률
					<c:out value="${sessionScope.list[2].ranking}" />

				</div>
				<img src="img/top_podium.svg" alt="Podium" class="podium_img">
			</div>

		</div>
	</div>
</body>
</html>