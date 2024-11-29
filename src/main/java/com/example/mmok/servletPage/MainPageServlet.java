package com.example.mmok.servletPage;

import com.example.mmok.Game.GameDao;
import com.example.mmok.Game.GameVo;
import com.example.mmok.Users.UserDao;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.List;

//내정보 몇전 몇승 몇패
//방만들기 버튼
//코드입장
//랭킹
@WebServlet("/MainPage")
public class MainPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


            request.getRequestDispatcher("/homePage2.jsp").forward(request, response);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        response.sendRedirect("/OmokPage");




    }
}

