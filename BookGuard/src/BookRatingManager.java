import java.sql.*;

public class BookRatingManager {
    public static void updateBookRating(int bookId, Connection con,double userrating) throws SQLException {


	    // Ensure the rating is converted to decimal format with one digit after the decimal
	    float userRating = (float) userrating;
	    userRating = Math.round(userRating * 10.0f) / 10.0f;

	    // Validate that the rating is within the range of 0.0 to 5.0
	    if (userRating < 0.0 || userRating > 5.0) {
	        System.out.println("Rating must be between 0.0 and 5.0.");
	        return;
	    }

	    // Query to fetch the current average rating and total ratings for the book
	    String query = "SELECT average_rating, total_ratings FROM books WHERE id = ?";
	    PreparedStatement st = con.prepareStatement(query);
	    st.setInt(1, bookId);
	    ResultSet rs = st.executeQuery();

	    if (rs.next()) {
	        float currentAverage = rs.getFloat("average_rating");
	        int totalRatings = rs.getInt("total_ratings");

	        // Calculate the new average rating based on the new rating
	        float newAverage = ((currentAverage * totalRatings) + userRating) / (totalRatings + 1);
	        totalRatings++;

	        // Round the new average to one decimal place
	        newAverage = Math.round(newAverage * 10.0f) / 10.0f;

	        // Update the book's rating in the database
	        String updateQuery = "UPDATE books SET average_rating = ?, total_ratings = ? WHERE id = ?";
	        PreparedStatement updateSt = con.prepareStatement(updateQuery);
	        updateSt.setFloat(1, newAverage);
	        updateSt.setInt(2, totalRatings);
	        updateSt.setInt(3, bookId);
	        updateSt.executeUpdate();

	        // Display the new average rating
	        System.out.println("Thank you for rating! The new average rating for this book is " + newAverage);
	    } else {
	        System.out.println("Book not found.");
	    }
	
    }
}
