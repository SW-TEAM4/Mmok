package com.example.mmok.rsp;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint("/game")
public class GameWebSocket {
    private static Set<Session> players = Collections.synchronizedSet(new HashSet<>());
    private static Map<Session, Boolean> playerReadyStatus = Collections.synchronizedMap(new HashMap<>());
    private static Map<Session, String> playerChoices = Collections.synchronizedMap(new HashMap<>());

    @OnOpen
    public void onOpen(Session session) throws IOException {
        if (players.size() >= 2) {
            session.close(new CloseReason(CloseReason.CloseCodes.TRY_AGAIN_LATER, "게임 인원이 초과되었습니다."));
            return;
        }
        players.add(session);
        playerReadyStatus.put(session, false); // 초기 상태는 준비되지 않음
        broadcast("새로운 플레이어가 참가했습니다! 현재 인원 : " + players.size() + "명");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if ("준비".equals(message)) {
            playerReadyStatus.put(session, true);
            broadcast("플레이어가 준비되었습니다! 현재 준비 인원 : " + getReadyCount() + "/" + players.size());
            checkAllReady();
        } else if (playerReadyStatus.getOrDefault(session, false)) {
            // 준비된 상태에서 가위바위보 선택
            playerChoices.put(session, message);
            if (playerChoices.size() == 2) { // 두 명이 선택을 완료했을 경우
                Iterator<Map.Entry<Session, String>> iterator = playerChoices.entrySet().iterator();
                Map.Entry<Session, String> player1 = iterator.next();
                Map.Entry<Session, String> player2 = iterator.next();

                Session session1 = player1.getKey();
                Session session2 = player2.getKey();
                String choice1 = player1.getValue();
                String choice2 = player2.getValue();

                String result1, result2;
                if (choice1.equals(choice2)) {
                    result1 = "무승부입니다! 다시 가위바위보를 선택하세요.";
                    result2 = "무승부입니다! 다시 가위바위보를 선택하세요.";
                } else if ((choice1.equals("가위") && choice2.equals("보")) ||
                        (choice1.equals("바위") && choice2.equals("가위")) ||
                        (choice1.equals("보") && choice2.equals("바위"))) {
                    result1 = "승리하였습니다! 선공입니다!";
                    result2 = "패배하였습니다! 후공입니다!";
                } else {
                    result1 = "패배하였습니다! 후공입니다!";
                    result2 = "승리하였습니다! 선공입니다!";
                }

                // 각각의 클라이언트에 결과 전송
                session1.getBasicRemote().sendText(result1);
                session2.getBasicRemote().sendText(result2);

                // 게임 초기화
                resetGame();
            } else {
                session.getBasicRemote().sendText("상대방의 선택을 기다리고 있습니다...");
            }
        } else {
            session.getBasicRemote().sendText("게임을 시작하기 위해 '준비' 버튼을 눌러주세요.");
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        players.remove(session);
        playerReadyStatus.remove(session);
        playerChoices.remove(session);
        broadcast("플레이어 한 명이 나갔습니다. 현재 인원 : " + players.size() + "명");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("오류 발생 : " + throwable.getMessage());
    }

    private void broadcast(String message) throws IOException {
        synchronized (players) {
            for (Session player : players) {
                player.getBasicRemote().sendText(message);
            }
        }
    }

    private int getReadyCount() {
        return (int) playerReadyStatus.values().stream().filter(ready -> ready).count();
    }

    private void checkAllReady() throws IOException {
        if (getReadyCount() == players.size() && players.size() == 2) {
            broadcast("모든 플레이어가 준비되었습니다! 가위바위보를 선택하세요.");
        }
    }

    private void resetGame() {
        playerChoices.clear();
        playerReadyStatus.replaceAll((session, ready) -> false); // 준비 상태 초기화
    }
}
