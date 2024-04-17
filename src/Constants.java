/** The {@code Constants} class provides constant values */
public class Constants {
  public static final String DEFAULT_CAPTCHA = "cJa3Ar4ERa";

  /* Private ensures that nobody can access the constructor of the method and if somehow they do, we will throw an exception as a double safety check */
  private Constants() {
    throw new AssertionError("Constants class should not be instantiated.");
  }
}
