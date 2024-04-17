/**
 * The {@code CaptchaService} interface provides methods for generating and verifying CAPTCHA
 */
public interface CaptchaService {
    /**
     * Generates a new CAPTCHA string.
     *
     * @return A String representing the generated CAPTCHA.
     */
    String generateCaptcha();
    /**
     * Verifies a given CAPTCHA string.
     *
     * @param captcha The CAPTCHA string to be verified.
     * @return {@code true} if the CAPTCHA is valid, {@code false} otherwise.
     */
    boolean verifyCaptcha(String captcha);
}
