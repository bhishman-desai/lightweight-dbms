/**
 * The {@code CaptchaServiceImplementation} class is an implementation of the {@code CaptchaService} interface
 */
public class CaptchaServiceImplementation implements CaptchaService {

  /**
   * Generates a new CAPTCHA string.
   *
   * @return A String representing the generated CAPTCHA. In this implementation, it returns the default CAPTCHA string.
   * @see Constants#DEFAULT_CAPTCHA
   */
  @Override
  public String generateCaptcha() {
    return Constants.DEFAULT_CAPTCHA;
  }

  /**
   * Verifies a given CAPTCHA string.
   *
   * @param captcha The CAPTCHA string to be verified.
   * @return {@code true} if the provided CAPTCHA matches the default CAPTCHA, {@code false} otherwise.
   * @see Constants#DEFAULT_CAPTCHA
   */
  @Override
  public boolean verifyCaptcha(String captcha) {
    return captcha.equals(Constants.DEFAULT_CAPTCHA);
  }
}
