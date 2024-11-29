package com.example.mmok;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GameRoomPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 요청 속성에서 게임 코드 가져오기
        String gameCode = (String) request.getAttribute("gameCode");

        if (gameCode != null) {
            // 게임 코드가 있으면 gamePage.jsp로 포워드
            request.setAttribute("gameCode", gameCode);
            request.getRequestDispatcher("gamePage.jsp").forward(request, response);
        } else {

            response.getWriter().write("게임 코드가 없습니다.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}