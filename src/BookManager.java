import java.sql.*;

public class BookManager {
    public static void showBooks() throws SQLException {
        Connection con = Database.getConnection();

        String query = "SELECT id, name, average_rating, avl FROM books WHERE avl='yes';";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        System.out.println("ID\tName\t\tAverage Rating");
        System.out.println("--------------------------------------");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            double averageRating = rs.getDouble("average_rating");
            
            System.out.printf("%d\t%-15s\t%.1f\n", id, name, averageRating);
        }

        con.close();
    }
}
