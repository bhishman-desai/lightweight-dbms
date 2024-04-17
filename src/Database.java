import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Database implements DataDefinition, DataManipulation, TransactionControl {
  private static final Logger logger = setupLogger();
  private static Database instance;
  private final UserAuthenticationManager userAuthenticationManager;
  private final TransactionManager transactionManager;
  private final DataSerializer dataSerializer = new CustomDelimiterDataSerializerImplementation();
  private QueryTransactionProcessor queryTransactionProcessor;

  private Database(
      UserAuthenticationManager userAuthenticationManager, TransactionManager transactionManager) {
    this.userAuthenticationManager = userAuthenticationManager;
    this.transactionManager = transactionManager;
  }

  /* Logger setup */
  private static Logger setupLogger() {
    Logger logger = Logger.getLogger(Database.class.getName());
    try {
      /* Check if the log file already exists */
      boolean append = Files.exists(Paths.get("files/database.log"));

      /* Configure the FileHandler */
      FileHandler fileHandler = new FileHandler("files/database.log", append);
      fileHandler.setFormatter(new SimpleFormatter());

      /* Add the FileHandler to the logger */
      logger.addHandler(fileHandler);

      /* Remove the default console handler */
      Logger rootLogger = Logger.getLogger("");
      Handler[] handlers = rootLogger.getHandlers();
      for (Handler handler : handlers) {
        rootLogger.removeHandler(handler);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return logger;
  }

  /**
   * Get the singleton instance of the Database.
   *
   * @param userAuthenticationManager User authentication manager
   * @param transactionManager Transaction manager
   * @return Singleton instance of the Database
   */
  public static synchronized Database getInstance(
      UserAuthenticationManager userAuthenticationManager, TransactionManager transactionManager) {
    if (instance == null) {
      instance = new Database(userAuthenticationManager, transactionManager);
    }
    return instance;
  }

  /**
   * @param tableName name of the table to be created
   * @param columns columns to be added
   */
  @Override
  public void createTable(String tableName, List<String> columns) {
    String filePath = "files/" + tableName.toLowerCase() + ".csv";

    File tableFile = new File(filePath);
    if (tableFile.exists()) {
      System.out.println("Table already exists!");
      return;
    }
    PersistentDataProcessor persistentDataProcessorForUser =
        new PersistentDataProcessor(filePath, dataSerializer);
    Data data = new Data(List.of(columns));
    persistentDataProcessorForUser.saveData(data);
    System.out.println("Table created successfully!");
    logger.info(
        "Table created: "
            + tableName
            + " with columns: "
            + columns
            + " by "
            + userAuthenticationManager.getCurrentUser());
  }

  /**
   * @param tableName name of the table to be dropped
   */
  @Override
  public void dropTable(String tableName) {
    String filePath = "files/" + tableName.toLowerCase() + ".csv";

    File tableFile = new File(filePath);
    if (tableFile.exists()) {
      if (tableFile.delete()) {
        System.out.println("Table " + tableName + " dropped successfully.");
        logger.info(
            "Table dropped: " + tableName + " by " + userAuthenticationManager.getCurrentUser());
      } else {
        System.out.println("Failed to delete table " + tableName);
      }
    } else {
      System.out.println("Table " + tableName + " does not exists!");
    }
  }

  /**
   * @param tableName name of the table
   * @param columns columns to be selected
   * @param condition condition of the query
   */
  @Override
  public void select(String tableName, List<String> columns, String condition) {

    String filePath = "files/" + tableName.toLowerCase() + ".csv";

    File tableFile = new File(filePath);
    if (tableFile.exists()) {

      PersistentDataProcessor persistentDataProcessorForSelect =
          new PersistentDataProcessor(filePath, dataSerializer);
      Data data = new Data(new ArrayList<>());
      persistentDataProcessorForSelect.loadData(data);

      DataTableProcessAndPrint dataTableProcessAndPrint = new DataTableProcessAndPrint();
      dataTableProcessAndPrint.printTable(data.getData(), columns, condition);
      logger.info(
          "Table queries: "
              + tableName
              + " with columns: "
              + columns
              + " and condition "
              + condition
              + " by "
              + userAuthenticationManager.getCurrentUser());

    } else {
      System.out.println("Table " + tableName + " does not exists!");
    }
  }

  /**
   * @param tableName name of the table
   * @param values values to be inserted in the table
   */
  @Override
  public void insert(String tableName, List<List<String>> values) {
    String filePath = "files/" + tableName.toLowerCase() + ".csv";

    File tableFile = new File(filePath);
    if (tableFile.exists()) {
      PersistentDataProcessor persistentDataProcessorForInsert =
          new PersistentDataProcessor(filePath, dataSerializer);
      Data data = new Data(new ArrayList<>());
      persistentDataProcessorForInsert.loadData(data);
      data.addData(values);

      persistentDataProcessorForInsert.saveData(data);

      System.out.println("Values inserted successfully!");
      logger.info(
          "Values inserted into "
              + tableName
              + " with values: "
              + values
              + " by "
              + userAuthenticationManager.getCurrentUser());

    } else {
      System.out.println("Table " + tableName + " does not exists!");
    }
  }

  /** Start of the transaction */
  @Override
  public void beginTransaction() {
    transactionManager.setInTransaction(true);
    System.out.println("Transaction started successfully!");
  }

  /** End of the transaction */
  @Override
  public void endTransaction() {
    transactionManager.setInTransaction(false);
    transactionManager.clear();
    System.out.println("Transaction stopped successfully!");
  }

  /** Committing the transaction */
  @Override
  public void commit() throws IncorrectQueryException {
    transactionManager.setInTransaction(false);
    for (String transactionQuery : transactionManager.getTransactionQueries()) {
      queryTransactionProcessor.processor(transactionQuery);
    }
    transactionManager.clear();
    System.out.println("Transaction committed successfully!");
  }

  /** Rollback the transaction */
  @Override
  public void rollback() {
    transactionManager.setInTransaction(false);
    transactionManager.clear();
    System.out.println("Transaction rollback successfully!");
  }

  public void catchQuery(String query) throws IncorrectQueryException {
    queryTransactionProcessor =
        new QueryTransactionProcessor(instance, userAuthenticationManager, transactionManager);
    queryTransactionProcessor.processor(query);
  }
}
