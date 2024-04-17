/**
 * The {@code IncorrectQueryException} class is a custom exception that indicates an error in the query syntax or semantics.
 */
public class IncorrectQueryException extends Exception {
  public IncorrectQueryException(String errorMessage) {
    super(errorMessage);
  }
}
