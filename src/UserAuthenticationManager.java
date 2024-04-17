/**
 * The {@code UserAuthenticationManager} class manages user authentication using a {@link UserService} for user-related operations
 * and a {@link CaptchaService} for captcha verification.
 */
public class UserAuthenticationManager {
  private final UserService userService;
  private final CaptchaService captchaService;
  private String currentUser;

  public UserAuthenticationManager(UserService userService, CaptchaService captchaService) {
    this.userService = userService;
    this.captchaService = captchaService;
  }

  public String getCurrentUser(){
    return currentUser;
  }

  public boolean authenticateUser(String userId, String password, String captcha) {
    /* Authenticate user using UserService */
    boolean isAuthenticated = userService.authenticateUser(userId, password);

    /* Verify captcha using CaptchaService */
    boolean isCaptchaValid = captchaService.verifyCaptcha(captcha);

    /* Return true only if both authentication and captcha verification succeed */
    currentUser = userService.getUserById(userId);
    return isAuthenticated && isCaptchaValid;
  }

  public void addUser(User user) {
    userService.addUser(user);
  }

  public void removeUser(String username) {
    userService.removeUser(username);
  }
}
