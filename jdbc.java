package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class jdbc {
    static String url = "jdbc:mysql://<серверийн хаяг>:3306/<өгөгдлийн сангийн нэр>";
    static String username = "bolormaa"; // MySQL сервер рүү нэвтрэх хэрэглэгчийн нэр
    static String password = "bolormaa-dbsys"; // MySQL сервер рүү нэвтрэх нууц үг
    static String query; // Бичих query хадгалах хувьсагч

    static Connection connection;
    static Statement statement;
    static ResultSet result;

    public static void main(String[] args) throws SQLException {
    	// try - catch блок ашиглан алдаа гарж буйг шалгах
        try {
        	// өгөгдлийн сантай холбогдох
            connection = DriverManager.getConnection(url, username, password);
            // statement үүсгэх
            statement = connection.createStatement();
            
            // хэрэглэгчээс хийх гэж буй үйлдэлийг нь аваад тохирсон функцийг ажиллуулах
            Scanner scanner = new Scanner(System.in);
            
            boolean validInput = false;
            
            while (!validInput) { // Хэрэглэгч буруу үйлдэл оруулвал дахин үйлдэл оруулах боломжтой
                System.out.println("Хийх үйлдэлээ оруулна уу? (insert, update, delete, select):");
                String
                function = scanner.nextLine();

                switch (function) {
                case "insert":
                    insertUser("Bolormaa", "Batjargal", 99657524, "bolormae1227@gmail.com", "test12345");
                    validInput = true;
                    break;
                case "update":
                    updatePlaygroundName(1, "City Sport Centre");
                    validInput = true;
                    break;
                case "delete":
                    deleteOrderItem(5);
                    validInput = true;
                    break;
                case "select":
                    selectPlaygrounds();
                    validInput = true;
                    break;
                default:
                    System.out.println("Буруу үйлдэл байна.");
                    break;
                }
            }
            // Програм ажиллаж дууссан учир серверийн холболт, гараас утга авахаа зогсоох
            scanner.close();
            connection.close();
        } catch (SQLException e) {
        	// Алдаа мэдээллэх
            e.printStackTrace();
        }
    }

    /**
     * Шинэ хэрэглэгч нэмэх функц (INSERT QUERY)
     */
    public static void insertUser(String firstName, String lastName, int phoneNumber, String email, String password) {
        try {
        	// Хэрэглэгч нэмэх query бэлдэх
        	query = "INSERT INTO Users (firstName, lastName, phoneNumber, email, password)" +
        			"VALUES(?, ?, ?, ?, ?)";
        	
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            // Бэлдсэн query-д тохирсон утгуудыг оноох
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, phoneNumber);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, password);

            /* 
             * Query гээ ажиллуулаад ирсэн датаг rows-д хадгалах ингэснээр
             * буцаж ирсэн дата 0 ээс их байвал амжилттай гэж шалгаж болно
             */ 
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                System.out.println("Хэрэглэгч амжилттай нэмэгдлээ.");
            }
        } catch (SQLException e) {
        	// Алдаа мэдээллэх
            e.printStackTrace();
        }
    }

    /**
     * Заалны нэр солих функц параметрээр тухайн нэр солих
     * заалны id, солих нэрийг авна (UPDATE QUERY)
     */
    public static void updatePlaygroundName(int id, String name) {
    	// Заалны нэр солих query бэлдэх
        query = "UPDATE PlayGrounds SET name = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            // Бэлдсэн query-д тохирсон утгуудыг оноох
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);

            /*
             * Query гээ ажиллуулаад ирсэн датаг rows-д хадгалах ингэснээр
             * буцаж ирсэн дата 0 ээс их байвал амжилттай гэж шалгаж болно
             */ 
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                System.out.println("Заалны нэр амжилттай солигдлоо.");
            }
        } catch (SQLException e) {
        	// Алдаа мэдээллэх
            e.printStackTrace();
        }
    }

    /**
     * OrderItem устгах параметрээр 
     * устгах гэж байгаа id авна (DELETE QUERY)
     */
    public static void deleteOrderItem(int id) {
    	// OrderItem устгах query бэлдэх
        query = "DELETE FROM OrderItems WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Бэлдсэн query-д тохирсон утга оноох
            preparedStatement.setInt(1, id);

            /*
             * Query гээ ажиллуулаад ирсэн датаг rows-д хадгалах ингэснээр
             * буцаж ирсэн дата 0 ээс их байвал амжилттай гэж шалгаж болно
             */ 
            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                System.out.println("Амжилттай устгагдлаа.");
            }
        } catch (SQLException e) {
        	// Алдаа мэдээллэх
            e.printStackTrace();
        }
    }
    // Select Query
    public static void selectPlaygrounds() {
        query = "SELECT p.id, p.name, p.phoneNumber, p.hourPrice, c.name AS category, d.name AS district, i.url AS image " +
	            "FROM PlayGrounds p " +
		            "LEFT JOIN Images i ON i.playgroundId = p.id " +
		            "LEFT JOIN Categories c ON c.id = p.categoryId " +
		            "LEFT JOIN Districts d ON d.id = p.districtid " +
	            "ORDER BY p.id ASC";
        try {
        	// Query гээ ажиллуулаад ирсэн датаг result-д хадгалах
            result = statement.executeQuery(query);
            // Ирсэн мөр бүрээр хэвлэх
            System.out.printf("| %-2s | %-30s | %-8s | %-10s | %-12s | %-12s | %-45s |\n",
					"ID", "Нэр", "Утас", "Цагийн үнэ", "Төрөл", "Хороо", "Зураг");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
            while (result.next()) {
                String id = result.getString("id");
                String name = result.getString("name");
                String phoneNumber = String.valueOf(result.getInt("phoneNumber"));
                String hourPrice = String.valueOf(result.getDouble("hourPrice"));
                String category = result.getString("category");
                String district = result.getString("district");
                String image = result.getString("image");
                System.out.printf("| %-2s | %-30s | %-8s | %-10s | %-12s | %-12s | %-45s |\n",
                					id, name, phoneNumber, hourPrice, category, district, image);
            }
        } catch (SQLException e) {
        	// Алдаа мэдээллэх
            e.printStackTrace();
        }
    }

}
