BookGuard - Your Smart Bookstore Assistant

Welcome to BookGuard, a comprehensive Java-based bookstore management system! This application enables seamless management of book rentals, returns, and inventory updates, while prioritizing user security through authentication. Follow this guide to set up and run the project smoothly.

---

Features
- User Authentication: Secure login to ensure authorized access.
- View Books: Browse through the available books in the store.
- Rent Books: Effortlessly rent your favorite books.
- Return Books: Return books and keep your account up-to-date.
- Rating System: Update book ratings based on your experience.
- Penalty Management: Automatic penalty calculation for delayed returns.

---

How to Run the Project
1. Clone the Repository  
   ```bash
   git clone https://github.com/<your-repository>/BookGuard.git
   cd BookGuard
   ```

2. Set Up the Database
   - Ensure you have MySQL installed.
   - Create a database named `bookstore`.
   - Import the provided SQL script file (`database.sql`) to set up tables and sample data.

3. Update Database Credentials
   Update the `DatabaseConnection.java` file with your MySQL credentials:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/bookstore";
   private static final String USER = "your-username";
   private static final String PASSWORD = "your-password";
   ```

4. Compile and Run the Application
   - Open the project in your favorite Java IDE or use the terminal.
   - Compile and run the `Menu.java` file to start the application.

5. Usage Instructions 
   - Log in using valid credentials (or register via SQL script for initial use).
   - Navigate through the menu options:
     - Option 1: View available books.
     - Option 2: Rent a book.
     - Option 3: Return a book and update ratings.
     - Option 4: Exit the application.

---

Prerequisites
- JDK 8 or above
- MySQL 5.7 or above
- IDE (optional but recommended)

---

Contributing
We welcome contributions to enhance BookGuard! Feel free to fork the repository and submit pull requests with your improvements.

---

Enjoy your journey with BookGuard, the smart way to manage your bookstore operations! 🌟
