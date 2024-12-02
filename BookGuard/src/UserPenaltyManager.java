import java.sql.*;

public class UserPenaltyManager {
    public static void updatePenaltyAndRatinguser(Connection con) throws SQLException {
	    String getUserDetails = "SELECT penalty, rating, limitofrent FROM users WHERE uname = ?";
	    PreparedStatement st = con.prepareStatement(getUserDetails);
	    st.setString(1, Menu.currentUser);
	    ResultSet rs = st.executeQuery();

	    if (rs.next()) {
	        int penalty = rs.getInt("penalty");
	        int rating = rs.getInt("rating");
	        int limit = rs.getInt("limitofrent");

	        penalty++;

	        boolean ratingReduced = false;
	        if ((rating == 5 && penalty >= 4) || (rating == 4 && penalty >= 3) || (rating == 3 && penalty >= 2)) {
	            rating--;
	            penalty = 0;
	            limit -= 2;
	            ratingReduced = true;
	        }

	        // Block the user if rating falls below 2
	        if (rating < 2) {
	            BlockManager.blockUser(con);
	            System.out.println("Your account has been blocked due to low rating. You will be unblocked after 10 days.");
	            return;
	        }

	        String updateUser = "UPDATE users SET penalty = ?, rating = ?, limitofrent = ? WHERE uname = ?";
	        PreparedStatement updateSt = con.prepareStatement(updateUser);
	        updateSt.setInt(1, penalty);
	        updateSt.setInt(2, rating);
	        updateSt.setInt(3, limit);
	        updateSt.setString(4, Menu.currentUser);
	        updateSt.executeUpdate();

	        if (ratingReduced) {
	            System.out.println("Your rating has been reduced to " + rating + " and your rental limit is now " + limit);
	        } else {
	            System.out.println("Your penalty has increased to " + penalty + ".");
	        }
	    }
	}
}
