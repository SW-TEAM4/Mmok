package com.example.mmok.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

//URL 매핑
@ServerEndpoint(value = "/websocket", configurator = HttpSessionConfigurator.class)
public class WebSocket {

	// 세션 목록
	private static final List<Session> sessions = new ArrayList<Session>();
	// 클라이언트 정보를 저장할 리스트
	private final List<String> clientInfoList = new ArrayList<>();
//    public String userColor = "";
	private static String currentTurn = "black";
	// 서버에서 관리할 보드 로직 및 보드 생성
	Logic logic = new Logic();

	// 특정 클라이언트 초기 바둑돌 전송
	private void sendTurn(Session client, String turn) throws IOException {
		JSONObject json = new JSONObject();
		json.put("type", "assign");
		json.put("turn", turn);
		client.getBasicRemote().sendText(json.toString());
	}

	// 모든 클라이언트에 메시지 전송
	private void broadcast(String message) throws IOException {
		for (Session client : sessions) {
			client.getBasicRemote().sendText(message);
		}
	}

	// WebSocket으로 브라우저가 접속하면 요청되는 함수
	@OnOpen
	public void handleOpen(Session sess) throws IOException {
		sessions.add(sess); // 세션 추가
		System.out.println("client is now connected...");

		// HttpSession 가져오기
		HttpSession httpSession = (HttpSession) sess.getUserProperties().get(HttpSession.class.getName());
		if (httpSession != null) {
			String userName = (String) httpSession.getAttribute("user_name");
			String userId = (String) httpSession.getAttribute("user_id");
			Object winAttribute = httpSession.getAttribute("win");
			Object loseAttribute = httpSession.getAttribute("lose");
			int lose = (loseAttribute != null) ? (int) loseAttribute : 0;
			int win = (winAttribute != null) ? (int) winAttribute : 0;
			System.out.println("HttpSession user_name: " + userName + win);
			System.out.println("HttpSession user_id: " + userId + lose);
			// JSON 생성
			JSONObject userJson = new JSONObject();
			userJson.put("type", "userInfo");

			if (sessions.size() == 1) {
				userJson.put("user1", new JSONObject()
							.put("name", userName)
							.put("win", win)
							.put("lose", lose)); // 프로필 이미지는 기본값 또는 세션에서 가져올 수
																							// 있음
			} else if (sessions.size() == 2) {
				userJson.put("user2", new JSONObject()
							.put("name", userName)
							.put("win", win)
							.put("lose", lose));
			}

			clientInfoList.add(userJson.toString()); // 정보 리스트에 추가
			
	        // 두 번째 유저가 들어왔을 때, clientInfoList의 모든 유저 정보를 브로드캐스트로 전송
	        broadcast(clientInfoList.toString());

		}else {
			System.out.println("httpsession이 널이야");
		}

		if (sessions.size() == 2) {
			System.out.println("session size == 2");
			sendTurn(sessions.get(0), "black");
			sendTurn(sessions.get(1), "white");
			currentTurn = "black";

			broadcast("{\"turn\": \"" + currentTurn + "\", \"message\": \"게임이 시작됩니다. 흑 플레이어 차례입니다.\"}");

		}
	}

	// WebSocket으로 메시지가 오면 요청되는 메서드
	@OnMessage
	public void handleMessage(Session receiveSession, String message) {
		System.out.println("Received message: " + message);

		try {
			// 메시지가 JSON 형식인지 확인하고 파싱
			JSONObject jsonMessage = new JSONObject(message);

			// 메시지의 타입에 따라 처리
			String type = jsonMessage.getString("type");

			// 게임 메시지 처리
			if ("game".equals(type)) {
				int row = jsonMessage.getInt("row");
				int col = jsonMessage.getInt("col");
				String color = jsonMessage.getString("color");

				System.out.println("row: " + row);
				System.out.println("col: " + col);
				System.out.println("color: " + color);

				System.out.println("receiveSession.getId(): " + receiveSession.getId());

				// 현재 본인의 턴이 아닌 경우 무시
				if (!color.equals(currentTurn)) {
					receiveSession.getBasicRemote().sendText("{\"error\": \"이 차례는 " + currentTurn + "입니다. 기다려주세요.\"}");
					return;
				}

				// 시간 초과로 인한 착수 실패
				if (row == -1 || col == -1) {
					// 턴 변경
					currentTurn = (color.equals("black")) ? "white" : "black";
					JSONObject cellPut = new JSONObject();
					cellPut.put("type", "put");
					cellPut.put("color", color);
					cellPut.put("row", row);
					cellPut.put("col", col);
					cellPut.put("time", 30);
					broadcast(cellPut.toString());
					return;
				}

				// 오목 게임 로직 처리
				boolean isWin = logic.makeMove(row, col, color);

				// 게임 승리 여부 체크
				if (isWin) {
					JSONObject gameEnd = new JSONObject();
					gameEnd.put("type", "end");
					gameEnd.put("player", color);
					broadcast(gameEnd.toString());
				} else {
					// 오목 착수 지시
					// 턴 변경
					currentTurn = (color.equals("black")) ? "white" : "black";
					JSONObject cellPut = new JSONObject();
					cellPut.put("type", "put");
					cellPut.put("row", row);
					cellPut.put("col", col);
					cellPut.put("color", color);
					cellPut.put("time", 30);
					// cellPut.put("currentTurn", currentTurn);
					broadcast(cellPut.toString()); // 서버로 요청 보낸 착수 정보를 다시 브로드 캐스트로 양쪽 세션에 착수
					// broadcast("{\"color\": \"" + color + "\", \"row\": " + row + ", \"col\": " +
					// col + "}");

				}
			}
			// 채팅 메시지 처리
			else if ("chat".equals(type)) {
				String chatMessage = jsonMessage.getString("message");

				// 다른 클라이언트에게 채팅 메시지 전송
				for (Session session : sessions) {
					if (!receiveSession.getId().equals(session.getId())) {
						JSONObject chatResponse = new JSONObject();
						chatResponse.put("type", "chat");
						chatResponse.put("message", chatMessage);
						chatResponse.put("user", "상대방");
						session.getBasicRemote().sendText(chatResponse.toString());
					} else {
						// 자신에게는 "나 : " 붙여서 전송
						JSONObject chatResponse = new JSONObject();
						chatResponse.put("type", "chat");
						chatResponse.put("message", chatMessage);
						chatResponse.put("user", "나");
						session.getBasicRemote().sendText(chatResponse.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// WebSocket과 브라우저가 접속이 끊기면 요청되는 함수
	@OnClose
	public void handleClose() {
		System.out.println("client is now disconnected...");
	}

	// WebSocket과 브라우저 간에 통신 에러가 발생하면 요청되는 함수
	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}
}
