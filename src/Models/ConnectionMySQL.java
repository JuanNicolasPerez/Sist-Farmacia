package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {

    private String database_name = "pharmacy_database";
    private String user = "root";
    private String password = "123456";

    private String url = "jdbc:mysql://localhost:3306/" + database_name;

    Connection conn = null;

    public Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            System.err.println("Ha ocurrido un ClassNotFoundException" + e.getMessage());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

}
