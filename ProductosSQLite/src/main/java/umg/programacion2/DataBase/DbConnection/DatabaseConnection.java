package umg.programacion2.DataBase.DbConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:C:\\Users\\Raquel\\Desktop\\db_telebot.db";

    public static Connection getConnection() throws SQLException {
        // Retornar a la conexión a la base de datos
        return DriverManager.getConnection(URL);
    }
}
