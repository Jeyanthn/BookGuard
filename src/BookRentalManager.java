import java.sql.*;
import java.util.Scanner;

public class BookRentalManager {
    public static void rentBook(Scanner sc) throws SQLException {
			Connection con = Database.getConnection();
			String checkLimitQuery = "SELECT limitofrent FROM users WHERE uname = ?";
			PreparedStatement limitCheckSt = con.prepareStatement(checkLimitQuery);
			limitCheckSt.setString(1, Menu.currentUser);
			ResultSet limitRs = limitCheckSt.executeQuery();

			if (limitRs.next()) {
			    int userLimit = limitRs.getInt("limitofrent");

			    if (userLimit <= 0) {
			        System.out.println("You have reached your rental limit. Please return a rented book to rent a new one.");
			        con.close();
			        return ;
			    }
			}
			BookManager.showBooks();
			System.out.print("Enter the Book ID to rent: ");
			int bookId = sc.nextInt();
			
			// query to store the date of rent and return date
			String rentQuery = "insert into duedates (id, uid, rdate, ddate) VALUES (?, ?, curdate(), date_add(curdate(), interval 7 day));";
			PreparedStatement pst = con.prepareStatement(rentQuery);
			pst.setInt(1, bookId);
			pst.setString(2, Menu.currentUser);
			pst.executeUpdate();
			
			// query to show what book you have rented
			String query="select name from books  where id=?;";
			PreparedStatement st =con.prepareStatement(query);
			st.setInt(1,bookId);
			ResultSet rs= st.executeQuery();
			if(rs.next()==false) {
				System.out.println("There is No Book with the ID: "+bookId);
			}
			else {
			System.out.print("\n\nThe book - '"+rs.getString(1)+"' is yours Mr."+Menu.currentUser);
			
			// updating the bin column
			
			String q2="update books set bin=bin-1 where id=?;";
			st =con.prepareStatement(q2);
			st.setInt(1,bookId);
			st.executeUpdate();
			
			// updating the bout column
			
			String q3="update books set bout=bout+1 where id=?;";
			st =con.prepareStatement(q3);
			st.setInt(1,bookId);
			st.executeUpdate();
			
			// to check if the book is available in database;
			
			String q4="select bin from books where id=?;";
			st=con.prepareStatement(q4);
			st.setInt(1, bookId);
			rs=st.executeQuery();
			rs.next();
			if(rs.getInt(1)==0)
			{
				String q5="update books set avl='no' where id=?;";
				st=con.prepareStatement(q5);
				st.setInt(1, bookId);
				st.executeUpdate();
			}
			
			query = "UPDATE users SET limitofrent = limitofrent - 1 WHERE uname = ?";
	        st = con.prepareStatement(query);
	        st.setString(1, Menu.currentUser);
	        st.executeUpdate();
			}
	        
			con.close();
		
    }

    public static void returnBook(Scanner sc) throws SQLException {
			if(showReturnBook()) {
			System.out.print("Enter the Book ID to return: ");
			int bookId = sc.nextInt();

			Connection con = Database.getConnection();
//        String query = "DELETE FROM rentals WHERE username = ? AND book_id = ?";
//        PreparedStatement st = con.prepareStatement(query);
//        st.setString(1, Menu.currentUser);
//        st.setInt(2, bookId);
//
//        int rows = st.executeUpdate();
//        if (rows > 0) {
//            System.out.println("Book returned successfully!");
//            BookRatingManager.updateBookRating(bookId, con);
//        } else {
//            System.out.println("Failed to return the book.");
//        }
			
			String query = "SELECT rdate, ddate, uid FROM duedates WHERE id = ? AND uid = ?;";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, bookId);
			st.setString(2, Menu.currentUser);
			ResultSet rs = st.executeQuery();
			
			if (rs.next()) {
			    Date rentDate = rs.getDate("rdate");
			    Date dueDate = rs.getDate("ddate");
			    Date currentDate = new Date(System.currentTimeMillis());

			    // Check if the book is overdue
			    boolean isOverdue = currentDate.after(dueDate);
			    if (isOverdue) {
			        System.out.println("The book is overdue! Increasing your penalty.");
			        // Increase the penalty
			        UserPenaltyManager.updatePenaltyAndRatinguser(con);
			    } else {
			        System.out.println("The book is being returned on time. Thank you!");
			    }
			    
			    // Ask for a rating
			    System.out.print("Rate the book (0.0 to 5.0): ");
			    double userRating = sc.nextDouble();
			    BookRatingManager.updateBookRating(bookId, con,userRating);
			    

			    // Insert the return details into the return_history table
			    String insertHistory = "INSERT INTO returnhistory (id, username, rentdate, duedate, returndate) VALUES (?, ?, ?, ?, ?);";
			    PreparedStatement pst = con.prepareStatement(insertHistory);
			    pst.setInt(1, bookId);
			    pst.setString(2, Menu.currentUser);
			    pst.setDate(3, rentDate);
			    pst.setDate(4, dueDate);
			    pst.setDate(5, currentDate);
			    pst.executeUpdate();

			    // Delete the record from the duedates table
			    String deleteDueDate = "DELETE FROM duedates WHERE id = ? AND uid = ?;";
			    pst = con.prepareStatement(deleteDueDate);
			    pst.setInt(1, bookId);
			    pst.setString(2, Menu.currentUser);
			    pst.executeUpdate();


			    // Update the bin column
			    String query1 = "UPDATE books SET bin = bin + 1 WHERE id = ?;";
			    st = con.prepareStatement(query1);
			    st.setInt(1, bookId);
			    st.executeUpdate();

			    // Update the bout column
			    String q2 = "UPDATE books SET bout = bout - 1 WHERE id = ?;";
			    st = con.prepareStatement(q2);
			    st.setInt(1, bookId);
			    st.executeUpdate();

			    // Check and update the availability status
			    String q3 = "SELECT bin FROM books WHERE id = ?;";
			    st = con.prepareStatement(q3);
			    st.setInt(1, bookId);
			    rs = st.executeQuery();
			    rs.next();
			    if (rs.getInt("bin") >= 1) {
			        String q4 = "UPDATE books SET avl = 'yes' WHERE id = ?;";
			        st = con.prepareStatement(q4);
			        st.setInt(1, bookId);
			        st.executeUpdate();
			    }
			    // to update the limit
			    String increaseLimit = "UPDATE users SET limitofrent = limitofrent + 1 WHERE uname = ?";
			    PreparedStatement updateSt = con.prepareStatement(increaseLimit);
			    updateSt.setString(1, Menu.currentUser);
			    updateSt.executeUpdate();
			   
			} else {
			    System.out.println("No matching record found for the provided book ID. Please check and try again.");
			}
			
			

			con.close();
			}
			
        
    }

    public static boolean showReturnBook() throws SQLException {
        Connection con = Database.getConnection();
        String query = "SELECT b.id, b.name FROM books b JOIN duedates d ON b.id = d.id WHERE d.uid = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, Menu.currentUser);
        ResultSet rs = st.executeQuery();

        boolean hasrent=false;
        while (rs.next()) {
        	System.out.println("Books currently rented by you:");
            int id = rs.getInt("id");
            String name = rs.getString("name");
            System.out.println("ID: " + id + ", Name: " + name);
            hasrent=true;
        }
        if(hasrent == false)
        {
        	System.out.println("You have not rented any books.");
	        return false;
        }

        con.close();
        return true;
    }
}
