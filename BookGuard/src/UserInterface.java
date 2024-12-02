import java.sql.SQLException;
import java.util.Scanner;
public class UserInterface {
	public static void handleOptions(int a, Scanner scan) throws SQLException {
		int t=a;
		 if(t==1)
			{
				BookManager.showBooks();
				System.out.println("Thats all the books we currently have:\n\n if anything else please select the below options:\n 1.Show books in store\n 2.Renting a book\n 3.Returning a book\n 4.Exit");
				System.out.print("The Choosen option is: ");
			}
		 else if(t==2)
			{
				BookRentalManager.rentBook(scan);
				System.out.println("\n\nIf anything else please select the below options:\n 1.Show books in store\n 2.Renting a book\n 3.Returning a book\n 4.Exit");
				System.out.print("The Choosen option is: ");
			}
		 else if(t==3)
			{
				BookRentalManager.returnBook(scan);
				System.out.println("\n\nIf anything else please select the below options:\n 1.Show books in store\n 2.Renting a book\n 3.Returning a book\n 4.Exit");
				System.out.print("The Choosen option is: ");
			}
	}

}
