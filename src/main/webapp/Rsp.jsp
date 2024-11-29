<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>가위바위보</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #000;
            color: #fff;
        }
        .choices, .ready {
            margin: 20px;
        }
        button {
            font-size: 1.5em;
            margin: 10px;
            padding: 10px 20px;
            cursor: pointer;
        }
        #message {
            margin-top: 20px;
            font-size: 1.2em;
        }
    </style>
    <link rel="shortcut icon" href="#">
</head>
<body>
    <h1>가위바위보 게임</h1>
    <div class="ready">
        <button id="readyButton" onclick="sendReady()">준비하기</button>
    </div>
    <div class="choices" style="display: none;">
        <button onclick="makeChoice('rock')">바위</button>
        <button onclick="makeChoice('paper')">보</button>
        <button onclick="makeChoice('scissors')">가위</button>
    </div>
    <div id="message"></div>

    <script>
        const socket = new WebSocket("ws://localhost:8090/rsp3/game");
        let isReady = false;
        let choiceMade = false;

        socket.onopen = function() {
            document.getElementById("message").innerText = "Waiting for another player to join...";
        };

        socket.onmessage = function(event) {
            const message = event.data;

            if (message.includes("모든 플레이어가 준비되었습니다")) {
                // 모든 플레이어가 준비 완료되었을 때
                document.getElementById("message").innerText = "모든 플레이어가 준비되었습니다! 가위바위보를 선택하세요.";
                document.querySelector(".ready").style.display = "none";
                document.querySelector(".choices").style.display = "block";
            } else {
                document.getElementById("message").innerText = message;
            }
        };

        socket.onclose = function() {
            document.getElementById("message").innerText = "Connection closed.";
        };

        function sendReady() {
            if (isReady) return;
            isReady = true;
            socket.send("준비");
            document.getElementById("readyButton").innerText = "준비 완료!";
            document.getElementById("readyButton").disabled = true;
            document.getElementById("message").innerText = "다른 플레이어의 준비를 기다리는 중...";
        }

        function makeChoice(choice) {
            if (choiceMade) return;
            choiceMade = true;
            socket.send(choice);
            document.getElementById("message").innerText = "You chose " + choice + ". Waiting for the result...";
        }
    </script>
</body>
</html>
