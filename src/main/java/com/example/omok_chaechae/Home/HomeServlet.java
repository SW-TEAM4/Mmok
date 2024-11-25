package com.example.omok_chaechae.Home;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<String[]> rankings = new ArrayList<>();
        rankings.add(new String[] { "1", "홍길동", "9승 1패" });
        rankings.add(new String[] { "2", "김길동", "7승 4패" });
        rankings.add(new String[] { "3", "최길동", "7승 7패" });

        request.setAttribute("rankings", rankings);
        request.getRequestDispatcher("/homepage.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inputValue = request.getParameter("inputValue");
        System.out.println("입력된 값: " + inputValue);

        doGet(request, response);
    }
}