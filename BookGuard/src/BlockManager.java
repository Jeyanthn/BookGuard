import java.sql.*;

public class BlockManager {
    public static boolean isUserBlocked(Connection con) throws SQLException {
        String query = "SELECT unblock_date FROM blocked_users WHERE uname = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, Menu.currentUser);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            Date unblockDate = rs.getDate("unblock_date");
            Date currentDate = new Date(System.currentTimeMillis());

            if (currentDate.after(unblockDate)) {
                unblockUser(con);
                return false;
            } else {
                System.out.println("Your account is blocked until " + unblockDate + ".");
                return true;
            }
        }
        return false;
    }

    public static void blockUser(Connection con) throws SQLException {
        String query = "INSERT INTO blocked_users (username, blocked_date, unblock_date) VALUES (?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 10 DAY))";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, Menu.currentUser);
        st.executeUpdate();

        String updateStatus = "UPDATE users SET status = 'blocked' WHERE uname = ?";
        PreparedStatement updateSt = con.prepareStatement(updateStatus);
        updateSt.setString(1, Menu.currentUser);
        updateSt.executeUpdate();
    }

    public static void unblockUser(Connection con) throws SQLException {
        String query = "DELETE FROM blocked_users WHERE username = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, Menu.currentUser);
        st.executeUpdate();

        String resetQuery = "UPDATE users SET rating = 2, limitofrent = 2, status = 'active' WHERE uname = ?";
        PreparedStatement resetSt = con.prepareStatement(resetQuery);
        resetSt.setString(1, Menu.currentUser);
        resetSt.executeUpdate();

        System.out.println("Your account has been unblocked. Your rating and rental limit have been reset.");
    }
}
