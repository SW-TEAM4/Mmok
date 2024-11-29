package com.example.mmok.Rangking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;




public class RangkingDAO {
    private PreparedStatement pstmt;
    private Connection con;

    private String driver = "oracle.jdbc.OracleDriver";
    private String url = "jdbc:oracle:thin:@localhost:1521/xe";
    private String user = "c##testuser";
    private String pwd = "test1234";

    public List<RangkingVO> listMembers() {
        List<RangkingVO> list = new ArrayList<>();
        try {
            connDB();

            String query = "SELECT *\n" +
                    "FROM (\n" +
                    "      SELECT ROUND((NVL(A.WIN_COUNT, 0) / (NVL(A.WIN_COUNT, 0) + NVL(A.LOSE_COUNT, 0))) * 100, 1) AS RANK,\n" +
                    "             A.USER_ID,\n" +
                    "             U.USER_NAME,\n" +
                    "             NVL(A.WIN_COUNT, 0) AS WIN_COUNT,\n" +
                    "             NVL(A.LOSE_COUNT, 0) AS LOSE_COUNT,\n" +
                    "             (NVL(A.WIN_COUNT, 0) + NVL(A.LOSE_COUNT, 0)) AS TOTAL_COUNT\n" +
                    "        FROM (\n" +
                    "              SELECT NVL(COUNT(DECODE(STATUS, 'W', 1)), 0) AS WIN_COUNT,\n" +
                    "                     NVL(COUNT(DECODE(STATUS, 'L', 1)), 0) AS LOSE_COUNT,\n" +
                    "                     USER_ID\n" +
                    "                FROM PARTNER\n" +
                    "               GROUP BY USER_ID\n" +
                    "             ) A\n" +
                    "        JOIN USER_TABLE U\n" +
                    "          ON A.USER_ID = U.USER_ID\n" +
                    "       ORDER BY RANK DESC\n" +
                    "     )\n" +
                    "WHERE ROWNUM <= 3 ";

            System.out.println(query);
            pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("USER_ID");
                String userName = rs.getString("USER_NAME");
                int winCount = rs.getInt("WIN_COUNT");
                int loseCount = rs.getInt("LOSE_COUNT");
                int totalCount = rs.getInt("TOTAL_COUNT");
                double ranking = rs.getDouble("RANK");

                RangkingVO vo = new RangkingVO();
                vo.setUserId(userId);
                vo.setUserName(userName);
                vo.setWinCount(winCount);
                vo.setLoseCount(loseCount);
                vo.setTotalCount(totalCount);
                vo.setRanking(ranking);
                
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
