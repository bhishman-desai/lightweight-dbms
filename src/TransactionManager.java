import java.util.ArrayList;
import java.util.List;

/**
 * The TransactionManager class manages the state and queries of a transaction.
 */
public class TransactionManager {
  private boolean isInTransaction;
  private List<String> transactionQueries = new ArrayList<>();

  public TransactionManager(boolean isInTransaction) {
    this.isInTransaction = isInTransaction;
  }

  public boolean isInTransaction() {
    return isInTransaction;
  }

  public void setInTransaction(boolean inTransaction) {
    this.isInTransaction = inTransaction;
  }

  public List<String> getTransactionQueries() {
    return transactionQueries;
  }

  public void setTransactionQueries(String transactionQuery) {
    this.transactionQueries.add(transactionQuery);
  }
  public void clear(){
    this.transactionQueries = new ArrayList<>();
  }
}
