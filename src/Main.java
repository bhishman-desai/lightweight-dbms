import java.util.Scanner;

public class Main {
  public static void main(String[] args) {

    /* User Authentication Manager */
    UserService userService = new UserServiceImplementation();
    CaptchaService captchaService = new CaptchaServiceImplementation();
    UserAuthenticationManager authenticationManager =
        new UserAuthenticationManager(userService, captchaService);

    /* Transaction Manager */
    TransactionManager transactionManager = new TransactionManager(false);

    boolean isLoggedIn = false;

    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to the Database Management System!");

    /* Ask for user credentials until successfully logged in */
    while (!isLoggedIn) {
      System.out.print("UserId: ");
      String userId = scanner.nextLine();

      System.out.print("Password: ");
      String password = scanner.nextLine();

      System.out.print("Captcha ----> " + captchaService.generateCaptcha());
      System.out.println();
      System.out.print("Enter Captcha: ");
      String captcha = scanner.nextLine();

      isLoggedIn = authenticationManager.authenticateUser(userId, password, captcha);

      if (!isLoggedIn) {
        System.out.println("Invalid credentials or Captcha. Please try again.");
      }
    }

    System.out.println("Successfully logged in!");
    System.out.println("You can now start writing queries.");

    /* Allow the user to write queries */
    while (true) {
      Database database = Database.getInstance(authenticationManager, transactionManager);

      System.out.print("Enter your query (or 'exit' to logout): ");
      String query = scanner.nextLine();

      if (query.equalsIgnoreCase("exit")) {
        System.out.println("Logged out successfully.");
        break;
      }

      try {
        database.catchQuery(query);
      } catch (IncorrectQueryException incorrectQueryException) {
        System.out.println(incorrectQueryException.getMessage());
      }
    }

    scanner.close();
  }
}
