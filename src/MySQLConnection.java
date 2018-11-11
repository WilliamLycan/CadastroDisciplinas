import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static MySQLConnection instance = null;
    private Connection connection = null;


    private MySQLConnection() {
        try {
            String driverName = "com.mysql.cj.jdbc.Driver";
            Class.forName(driverName);

            String server = "localhost:";
            String port = "3306/";
            String db = "ead";
            String url = "jdbc:mysql://" + server + port + db + "?useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";

            // ajuste o username e password de acordo com o que voce utiliza para acessar o banco
            String username = "root";
            String password = "root";

            connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                System.out.println("STATUS---> Conectado com sucesso!");
            } else {
                System.err.println("STATUS---> Não foi possível realizar conexão.");
            }
            connection.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            System.out.println("O driver especificado não foi encontrado.");
        } catch (SQLException e) {
            System.out.println("Não foi possível conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    public static MySQLConnection getInstance() {
        if (instance == null) {
            instance = new MySQLConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}