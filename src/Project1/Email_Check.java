package Project1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Email_Check {
    String email;
    String path;

    public Email_Check(String email, String path) {
        this.email = email;
        this.path = path;
    }

    public final boolean check() throws SQLException {
        //如果数据库中存在该用户，则返回True，如果不存在，则返回Flase
        Connection conn = null;
        boolean ans = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            DB_Connect connect = new DB_Connect();
            conn = connect.connect(path);
            String sql = "SELECT * FROM mypan.users WHERE Email=?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            ans = rs.next();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return ans;
    }
}
