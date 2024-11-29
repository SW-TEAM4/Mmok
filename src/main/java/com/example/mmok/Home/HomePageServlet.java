package com.example.mmok.Home;

import java.io.IOException;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.mmok.Game.GameDao;
import com.example.mmok.Game.GameVo;
import com.example.mmok.Users.UserDao;

@WebServlet("/GameRoomPage")
public class HomePageServlet extends HttpServlet {
    private GameDao gameDao = new GameDao();
    private UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = request.getParameter("action");
        String room_hash = request.getParameter("hash");
        
        // 세션으로 부터 데이터 가져오기
        HttpSession session = request.getSession();
        String user_name = (String)session.getAttribute("user_name");
        String user_id = (String)session.getAttribute("user_id");
        
        if ("createRoom".equals(action)) {
        	if(user_name == null|| user_id == null) {
        		request.getRequestDispatcher("login.jsp").forward(request, response);
        	}
            // SecureRandom을 이용하여 6자리 문자열 생성
            String randomCode = generateSecureRandomCode();

            // GameVo 객체 생성 및 설정
            GameVo gameVo = new GameVo();
            gameVo.setUser_id1(user_id);
            gameVo.setGame_code(randomCode);

            // 방 생성
            gameDao.createGameRoom(gameVo);

            // 생성된 방 코드 저장 후 gamePage.jsp로 전달
            request.setAttribute("id",user_id);
            request.setAttribute("user_name1", user_name);
            request.setAttribute("gameCode", randomCode);
            request.setAttribute("message", "게임방 생성되었습니다!");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;

        } // 두 번째 유저가 게임방에 입장하는 경우
        else if ("joinRoom".equals(action)) {
            //코드 일치 여부
            if (!gameDao.checkGameCode(room_hash)){
                request.setAttribute("errorMessage2", "입장 코드가 존재하지 않습니다.");
                request.getRequestDispatcher("homePage.jsp").forward(request, response);
                return;
            }
            // 방 가득 찼는지 여부
            if(gameDao.isFull(room_hash)){
                request.setAttribute("errorMessage3","방이 가득 찼습니다.");
                request.getRequestDispatcher("homePage.jsp").forward(request,response);
                return;
            }

            // 두 번째 유저 게임방에 추가
            gameDao.secondInsertGameRoom(user_id, room_hash);

            // 입장 성공 후 gameRoom.jsp로 전달

            request.setAttribute("id",user_id);
            request.setAttribute("user2_name", user_name);
            request.setAttribute("gameCode", room_hash);
            request.setAttribute("message", "게임 방에 입장하셨습니다!");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }



    // 6자리 랜덤 문자열 생성 메서드
    private String generateSecureRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }


}