/**
 * The {@code UserService} interface defines methods for managing user-related operations, including user authentication,
 * adding users, and removing users.
 */
public interface UserService {
  boolean authenticateUser(String username, String password);

  void addUser(User user);

  void removeUser(String username);
  String getUserById(String userId);
}
