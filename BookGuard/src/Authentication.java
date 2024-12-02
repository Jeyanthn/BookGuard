import java.sql.*;

public class Authentication {
	public static boolean authenticateUser(String username,String password) throws SQLException {
			
			Connection con = Database.getConnection();

			// Check if the user is blocked
			if (BlockManager.isUserBlocked(con)) {
				con.close();
//				scan.close();
				return false;
			}

			String query = "SELECT * FROM users WHERE uname = ? AND password = ?;";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, username);
			st.setString(2, password);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				System.out.println("\nLogin successful! Welcome, " + username);
				Menu.currentUser = username;
				con.close();
//				scan.close();
				return true;
			} else {
				System.out.println("\nInvalid username or password.");
				con.close();
//				scan.close();
				return false;
			}

	}
}
