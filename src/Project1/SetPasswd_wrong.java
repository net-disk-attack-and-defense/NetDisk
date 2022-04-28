package Project1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetPasswd_wrong {
    public static int set(Connection conn, String ip, String num, String YMD, String hour, String minute) throws SQLException {
        PreparedStatement ps =null;
        String sql = "UPDATE mypan.passwd_wrong SET YMD=?, hour=?, minute=?, num=? WHERE ip=?;";
        ps = conn.prepareStatement(sql);
        ps.setString(1, YMD);
        ps.setString(2, hour);
        ps.setString(3, minute);
        ps.setString(4, num);
        ps.setString(5, ip);
        return ps.executeUpdate();
    }
}
