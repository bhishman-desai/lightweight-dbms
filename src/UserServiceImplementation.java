import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code UserServiceImplementation} class implements the {@link UserService} interface and
 * provides functionality for authenticating users, adding users, and removing users using a
 * persistent data source.
 */
public class UserServiceImplementation implements UserService {
  DataSerializer dataSerializer = new CustomDelimiterDataSerializerImplementation();
  PersistentDataProcessor persistentDataProcessorForUsers =
      new PersistentDataProcessor("files/users.csv", dataSerializer);
  PasswordHasher passwordHasher;

  @Override
  public boolean authenticateUser(String userId, String password) {
    Data data = new Data(new ArrayList<>());
    this.persistentDataProcessorForUsers.loadData(data);

    for (List<String> user : data.getData()) {
      passwordHasher = new PasswordHasher(password);
      if (user.get(0).equals(userId) && user.get(2).equals(passwordHasher.getHashedPassword())) {
        return true; /* Authentication successful */
      }
    }
    return false; /* Authentication failed */
  }

  @Override
  public void addUser(User user) {
    Data data = new Data(new ArrayList<>());
    this.persistentDataProcessorForUsers.loadData(data);
    passwordHasher = new PasswordHasher(user.getPassword());

    List<List<String>> existingData = data.getData();

    int nextUserId = 1;
    if (!existingData.isEmpty()) {
      /* Get the user ID of the last record and increment by 1 */
      nextUserId = Integer.parseInt(existingData.getLast().getFirst()) + 1;
    }

    /* Create a new user with the dynamically generated user ID */
    List<String> newUser =
        Arrays.asList(
            String.valueOf(nextUserId),
            user.getUsername(),
            passwordHasher.getHashedPassword(),
            user.getEmail());

    /* Add the new user to the existing data */
    existingData.add(newUser);

    /* Update the data and save */
    data.setData(existingData);
    this.persistentDataProcessorForUsers.saveData(data);
  }

  @Override
  public void removeUser(String username) {
    Data data = new Data(new ArrayList<>());
    this.persistentDataProcessorForUsers.loadData(data);

    /* Find the index of the user with the specified username */
    int indexToRemove = -1;
    for (int i = 0; i < data.getData().size(); i++) {
      List<String> user = data.getData().get(i);
      if (user.size() > 1 && user.get(1).equalsIgnoreCase(username)) {
        indexToRemove = i;
        break;
      }
    }

    /* Remove the user if found */
    if (indexToRemove != -1) {
      data.getData().remove(indexToRemove);
      this.persistentDataProcessorForUsers.saveData(data);
      System.out.println("User dropped successfully.");
    } else {
      System.out.println("User not found.");
    }
  }

  /**
   * @param userId id of the user
   * @return username of the user
   */
  @Override
  public String getUserById(String userId) {
    Data data = new Data(new ArrayList<>());
    this.persistentDataProcessorForUsers.loadData(data);

    /* Find the index of the user with the specified userId */
    int indexToRemove = -1;
    for (int i = 0; i < data.getData().size(); i++) {
      List<String> user = data.getData().get(i);
      if (user.size() > 1 && user.getFirst().equalsIgnoreCase(userId)) {
        indexToRemove = i;
        break;
      }
    }

    if (indexToRemove != -1) {
      return data.getData().get(indexToRemove).get(1);
    }

    return null;
  }
}
