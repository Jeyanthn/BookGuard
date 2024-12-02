import java.sql.SQLException;
import java.util.Scanner;
public class Menu {
    public static String currentUser = ""; // Static variable to store the username

    public static void main(String[] args) throws SQLException {
    	Scanner scan = new Scanner(System.in);
    	System.out.print("Enter Username: ");
		String username = scan.nextLine();
		System.out.print("Enter Password: ");
		String password = scan.nextLine();
        if (Authentication.authenticateUser(username,password)) {
            System.out.println("Welcome to our Bookstore\nNow Select the option to your desire:\n 1.Show books in store\n 2.Renting a book\n 3.Returning a book\n 4.Exit");
            int a = scan.nextInt();
            while (a <= 4) {
            	if(a < 4) {
            	   UserInterface.handleOptions(a,scan);
                }
            	else if (a == 4) {
                System.out.println("Thank you for using our bookstore\n Please come back soon!");
                break;
                }	
            	a=scan.nextInt();
            }
            if(a>4)
            {
            	System.out.println("Invalid Input");
            }
        }
        else {
           System.out.println("Authentication failed. Exiting the application.");
        }
        scan.close();

}
}
