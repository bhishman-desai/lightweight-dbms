/**
 * The {@code TransactionControl} interface defines methods for managing transactions in a database system.
 */
public interface TransactionControl {
  void beginTransaction();

  void endTransaction();

  void commit() throws IncorrectQueryException;

  void rollback();
}
