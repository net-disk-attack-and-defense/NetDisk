package Project1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Connect {
    public Connection connect(String path) throws SQLException, ClassNotFoundException, IOException {
/*
        FileInputStream in = new FileInputStream(path);
        Properties prop = new Properties();
        prop.load(in);
        String driver=prop.getProperty("driver");
        String my_url=prop.getProperty("my_url");
        String my_user=prop.getProperty("my_user");
        String my_password=prop.getProperty("my_password");
*/
        //需要把此处信息改为自己的
        String driver = "com.mysql.cj.jdbc.Driver";
        String my_url = "jdbc:mysql://127.0.0.1:3306/netdiskdata?useUnicode=true&characterEncoding=utf-8";
        String my_user = "root";
        String my_password = "lei003427";
        Class.forName(driver);
        return DriverManager.getConnection(my_url, my_user, my_password);
    }
}
