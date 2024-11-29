package com.example.mmok.Login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class checkDuplicateServlet
 */
@WebServlet("/checkDuplicate")
public class checkDuplicateServlet extends HttpServlet {

    private PreparedStatement pstmt;
    private Connection con;

    private String driver = "oracle.jdbc.OracleDriver";
    private String url = "jdbc:oracle:thin:@localhost:1521/xe";
    private String user = "testuser";
    private String pwd = "test1234";

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        connDB();
        String user_id = request.getParameter("user_id");
        try  {
            connDB();
            String query = "SELECT COUNT(*) FROM USERS WHERE user_id = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1,user_id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            int count = rs.getInt(1);

            PrintWriter out = response.getWriter();
            if (count > 0) {
                out.print("unavailable"); // 이미 사용 중인 아이디
            } else {
                out.print("available"); // 사용 가능한 아이디
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void connDB() {
        try {
            Class.forName(driver);
            System.out.println("드라이버 성공");
            con = DriverManager.getConnection(url, user, pwd);
            System.out.println("연결성공");
            //stmt = con.createStatement();
            System.out.println("stmt 생성 성공");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
