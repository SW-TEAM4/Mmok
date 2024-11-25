package com.example.omok_chaechae.Rangking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class RangkingDAO {
    private PreparedStatement pstmt;
    private Connection con;

    private String driver = "oracle.jdbc.OracleDriver";
    private String url = "jdbc:oracle:thin:@localhost:1521/xe";
    private String user = "testuser";
    private String pwd = "test1234";

    //private DataSource dataFactory;

    public List<RangkingVO> listMembers() {
        List<RangkingVO> list = new ArrayList<>();
        try {
            connDB();
           // con = dataFactory.getConnection();
            String query = "SELECT USER_NAME, WIN_COUNT "
                    + "FROM (SELECT USER_NAME, WIN_COUNT FROM USERS ORDER BY WIN_COUNT DESC) "
                    + "WHERE ROWNUM <= 3";
            System.out.println(query);
            pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userName = rs.getString("user_name");
                int winCount = rs.getInt("win_count");

                RangkingVO vo = new RangkingVO();
                vo.setUserName(userName);
                vo.setWinCount(winCount);
                list.add(vo);
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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
