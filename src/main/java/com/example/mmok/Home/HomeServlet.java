package com.example.mmok.Home;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 최초의 유저가 방만들기 눌렀음
    	
    	String inputValue = request.getParameter("inputValue");
        System.out.println("입력된 값: " + inputValue);
        
        doGet(request, response);
    }
}