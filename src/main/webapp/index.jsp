<%--
  Created by IntelliJ IDEA.
  User: 이윤채
  Date: 2024-11-25
  Time: 오후 9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<title>Omok Game</title>
<link rel="stylesheet" href="css/omok.css">
</head>
<body>
	<div class="game-container">
		<!-- 왼쪽 유저 정보 -->
		<div class="room-info">
			Room # <span id="room-code"> <%=request.getAttribute("gameCode") != null ? request.getAttribute("gameCode") : "N/A"%>
			</span>
		</div>
		<div class="user-info-left">
			<div class="user">
				<img src="img/user_profile1.svg" alt="User 1" class="user-img">
				<p class="user-name" id="user1-name"></p>
				<p class="user-record" id="user1-record"></p>
				<button class="ready-button" id="user1-ready-btn">준비완료</button>
			</div>
		</div>

		<!-- 가운데 게임판 및 채팅 -->
		<div class="game-board-container">
			<div class="game-header">
				<div id="timer">30</div>
				<div id="turn-info">상대방을 기다리는 중...</div>
			</div>
			<div id="board" class="board">
				<!-- 오목판이 이곳에 들어갑니다 -->
			</div>
			<div class="chat-container">
				<div id="messageBox" class="message-box">
					<textarea id="messageTextArea" rows="5" cols="54" readonly></textarea>
				</div>
				<div class="input-container">
					<input id="textMessage" type="text" class="message-input"
						placeholder="메시지를 입력하세요..." onkeydown="enter_check();">
					<button onclick="sendMessage()" class="send-button">Send</button>
				</div>
			</div>
		</div>

		<!-- 오른쪽 유저 정보 -->
		<div class="user-info-right">
			<div class="user">
				<img src="img/rangking_user2.svg" alt="User 2" class="user-img">
				<p class="user-name" id="user2-name"></p>
				<p class="user-record" id="user2-record"></p>
				<button class="ready-button" id="user2-ready-btn">준비완료</button>
			</div>
		</div>
	</div>
	<%
	request.setCharacterEncoding("utf-8");
	String id = request.getParameter("id");
	//String roomId = request.getParameter("roomId");
	%>
	<script>
  const board           = document.getElementById('board');
  const turnInfo        = document.getElementById('turn-info');
  const messageTextArea = document.getElementById("messageTextArea");
  const webSocket       = new WebSocket("ws://localhost:8090/omok_chaechae/websocket");
  const timerS = document.getElementById("timer");
  //let currentTurn = 'black';
  let turn = null; // 서버에서 받은 턴 정보 (color)
  let myTurn = false; // 내 차례인지 확인
  let timer = 30;
  let countdownInterval = null;
  let isAlertShown = false; // alert
  let gameBoard = Array(15).fill().map(() => Array(15).fill(null));

  // 15x15 바둑판 셀 생성
  for (let i = 0; i < 15; i++) {
    for (let j = 0; j < 15; j++) {
      const cell = document.createElement('div');
      cell.classList.add('cell');
      cell.dataset.row = i;
      cell.dataset.col = j;
      cell.addEventListener('click', handleCellClick);
      board.appendChild(cell);
    }
  }

  function handleCellClick(event) {
    const row = event.target.dataset.row;
    const col = event.target.dataset.col;
    // 내 차례인지 체크
	if(!myTurn){
		alert("지금은 당신의 차례가 아닙니다!");
		return;
	}
	// 착수 가능한지
	if(gameBoard[row][col]){
		alert("거기 착수 불가");
	  	return;
	}
    
	// 착수 전송
    sendMove(row, col);
    gameBoard[row][col] = turn;
    event.target.appendChild(createStone(turn));
    
    // 시간 멈춤
    clearInterval(countdownInterval);
  }
  
  // 타이머 갱신 함수 호출
  function startTimer() {
	  countdownInterval = setInterval(updateTimer, 1000); // 1초마다 updateTimer 호출
  }
  
  // 시간안에 아무것도 못하면 -1,-1
  function updateTimer(){
	  timer--;
	  timerS.textContent = timer; // 화면에 업데이트
	  if(timer <= 0 && !isAlertShown && myTurn){
		  isAlertShown = true;
		  clearInterval(countdownInterval); // 기존 타이머 종료
		  alert("제한시간 초과");
		  const message = {
		    type: "game",
		    row: -1,
		    col: -1,
		  	color: turn
		  };
		  webSocket.send(JSON.stringify(message)) // 턴 종료 전달
	  }
  }
  
  // 서버에 착수 요청 
  function sendMove(row, col){
	  // 서버로 착수 요청 전송
	  const message = {
	    type: "game",
	    row: row,
	    col: col,
	    color: turn // 자신의 색상
	  };

	  webSocket.send(JSON.stringify(message));
  }
  
  function createStone(color) {
    const stone = document.createElement('div');
    stone.classList.add(color);
    return stone;
  }


  // 서버와 연결되었을 때 실행되는 함수
  webSocket.onopen = function () {
    messageTextArea.value += "Server connected...\n";
    // updateBoardState();
    connection();
  };

  webSocket.onclose = function () {
    messageTextArea.value += "Server disconnected...\n";
  };

  webSocket.onerror = function () {
    messageTextArea.value += "Error occurred...\n";
  };

  // 서버로부터 메시지가 왔을 때
  webSocket.onmessage = function (message) {
    console.log(message);
    try {
      var gameState = JSON.parse(message.data);
      console.log("Parsed game state:", gameState);
      // 처음에 양쪽 클라이언트 정보 받아와서 화면 생성
      if(gameState.type === "userInfo"){
   		  updateUserInfo(gameState.user1, gameState.user2);
      }else if(Array.isArray(gameState)){
    	  gameState.forEach(userInfo =>{
    		 const user = JSON.parse(userInfo);
    		 if (user.user1) {
    	          updateUserInfo(user.user1, user.user2);
    	     }
    	  });
      }

      // 맨 처음 자신의 돌 색깔 할당
      if (gameState.type === "assign") {
          // Assign color and determine if it's my turn
          turn = gameState.turn;
          myTurn = (turn === "black"); // 흑돌이면 첫 턴
          turnInfo.textContent = myTurn ? "나의 차례입니다." : "상대방의 차례입니다.";
          startTimer(); // 타이머 시작
      }
      // 게임 종료
      if (gameState.type === "end"){
    	 const winner = gameState.player; // 승리한 플레이어 색깔
    	 alert(winner + " player wins!"); // 알림 표시
    	 endGame();
      }
      if (gameState.type === "put"){
    
   	  	const row       = gameState.row;
		const col       = gameState.col;
        const color     = gameState.color;
    	const time 		= gameState.time;
        //const currentTurn = gameSTate.currentTurn;
        const cell = board.querySelector(`[data-row="\${row}"][data-col="\${col}"]`);
 	
        // 보드판 업데이트
        if(cell){
        	cell.appendChild(createStone(color));  // 돌을 놓는다
        }
        // 서버로 부터 받아온 현재 턴 갱신
		myTurn = !myTurn;
		turnInfo.textContent = myTurn ? "나의 차례입니다." : "상대방의 차례입니다.";
		// 서버로부터 받은 time으로 timer 변수 업데이트
		if(myTurn){
			timer = time; // 타이머 갱신
			startTimer(); // 타이머 시작
			isAlertShown = false;
		}else{
			clearInterval(countdownInterval);
		}

		
      } else if (gameState.type === "chat") {
        messageTextArea.value += gameState.user + " : " + gameState.message + "\n";
      }
    } catch (e) {
      console.error("Invalid message format:", message.data);
      //messageTextArea.value += "Error: Invalid message format received.\n";
    }
  };

  function connection() {
    const id = "<%=id%>";
    const message = id + "님이 입장하셨습니다.";
    messageTextArea.value += message + "\n";
    // 서버로 입장 메시지 전송 (채팅)
    webSocket.send(JSON.stringify({ type: "chat", message: message }));
  }

  // 채팅 메시지 전송 함수
  function sendMessage() {
    const message = document.getElementById("textMessage").value;
    if (message.trim()) {
      // 채팅 메시지 전송
      webSocket.send(JSON.stringify({ type: "chat", message: message }));
      document.getElementById("textMessage").value = ""; // 입력창 초기화
    }
  }

  // Enter 키로 채팅 전송
  function enter_check() {
    if (event.keyCode == 13) { // Enter key
      sendMessage();
    }
  }
  
  //게임 종료 처리 함수
  function endGame() {
    // 게임 보드 클릭 비활성화
    const boardCells = document.querySelectorAll(".cell");
    boardCells.forEach((cell) => {
      cell.style.pointerEvents = "none"; // 클릭 불가 설정
    });

    // 턴 정보 업데이트
    const turnInfo = document.getElementById("turn-info");
    if (turnInfo) {
      turnInfo.textContent = "게임이 종료되었습니다.";
    }

    // 필요에 따라 WebSocket 종료
    if (webSocket) {
      webSocket.close();
    }
  }
  
  //유저 정보 갱신
  function updateUserInfo(user1, user2) {
    // User 1 정보
    document.getElementById("user1-name").textContent = user1.name;
    document.getElementById("user1-record").textContent = `전적: ${user1.win}승 ${user1.lose}패`;

    // User 2 정보
    document.getElementById("user2-name").textContent = user2.name;
    document.getElementById("user2-record").textContent = `전적: ${user2.win}승 ${user2.lose}패`;
  }
  
</script>
</body>
</html>
