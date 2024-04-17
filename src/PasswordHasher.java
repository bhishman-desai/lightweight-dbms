import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
  private final String hashedPassword;

  public PasswordHasher(String password) {
    this.hashedPassword = hashPasswordMD5(password);
  }

  public String getHashedPassword() {
    return hashedPassword;
  }

  private String hashPasswordMD5(String password) {
    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      System.out.println("Hashing failed!");
    }
    messageDigest.update(password.getBytes());
    byte[] bytes = messageDigest.digest();
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
