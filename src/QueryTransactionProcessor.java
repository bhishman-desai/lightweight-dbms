import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code QueryTransactionProcessor} class processes queries and performs corresponding
 * operations on db.
 */
public class QueryTransactionProcessor {
  private final Database database;
  private final UserAuthenticationManager userAuthenticationManager;
  private final TransactionManager transactionManager;

  public QueryTransactionProcessor(
      Database database,
      UserAuthenticationManager userAuthenticationManager,
      TransactionManager transactionManager) {
    this.database = database;
    this.userAuthenticationManager = userAuthenticationManager;
    this.transactionManager = transactionManager;
  }

  /**
   * Processes the given query.
   *
   * @param query The query to be processed.
   * @throws IncorrectQueryException If the query is incorrect or cannot be processed.
   */
  public void processor(String query) throws IncorrectQueryException {
    long startTime = System.currentTimeMillis();
    /* Driver code */
    try {

      if (!query.trim().endsWith(";")) {
        System.out.println("Please add ; at the end.");
        return;
      }

      /* Transaction related queries */
      if (query.toLowerCase().startsWith("begin transaction")) {
        database.beginTransaction();
        return;
      }
      if (query.toLowerCase().startsWith("end transaction")) {
        database.endTransaction();
        return;
      }
      if (query.toLowerCase().startsWith("rollback")) {
        database.rollback();
        return;
      }
      if (query.toLowerCase().startsWith("commit")) {
        database.commit();
        return;
      }

      /* If the transaction is  still going on, keep adding queries to list */
      if (transactionManager.isInTransaction()) {
        transactionManager.setTransactionQueries(query);
        System.out.println("Query added to transaction");
      }
      /* If the transaction is off */
      else {
        if (query.toLowerCase().startsWith("create table")) {
          createTableProcessor(query);
          return;
        }
        if (query.toLowerCase().startsWith("create user")) {
          createUserProcessor(query);
          return;
        }
        if (query.toLowerCase().startsWith("drop table")) {
          dropTableProcessor(query);
          return;
        }
        if (query.toLowerCase().startsWith("drop user")) {
          dropUserProcessor(query);
          return;
        }
        if (query.toLowerCase().startsWith("insert into")) {
          insertIntoTableProcessor(query);
          return;
        }
        if (query.toLowerCase().startsWith("select")) {
          selectFromTableProcessor(query);
          return;
        }

        throw new IncorrectQueryException("Error processing query " + query);
      }

    } catch (Exception exception) {
      throw new IncorrectQueryException("Error processing query " + query);
    }
    finally{
      long endTime = System.currentTimeMillis();
      long executionTime = endTime - startTime;

      System.out.println();
      System.out.println("Query execution time: " + executionTime + " milliseconds");
      System.out.println();
    }
  }

  /**
   * Processes a CREATE TABLE query by extracting the table name and columns, and then calls the
   * {@link Database#createTable(String, List)} method with the extracted table name and columns.
   *
   * @param query the CREATE TABLE query to be processed
   */
  private void createTableProcessor(String query) {
    /* Extract table name and columns from the query */
    String[] parts = query.split("\\(");
    String tableName = parts[0].substring("create table".length()).trim();
    tableName = tableName.split(" ")[0]; /* Get only the table name, ignoring any other tokens */
    List<String> columns =
        extractColumns(parts); /* Split the column part by comma and trim each column */

    /* Call createTable method with extracted table name and columns */
    database.createTable(tableName, columns);
  }

  /**
   * Processes a CREATE USER query by validating the syntax, extracting the username and password,
   * and adding a new user to the authentication manager.
   *
   * @param query The CREATE USER query to be processed.
   * @throws IncorrectQueryException If the query syntax is incorrect.
   */
  private void createUserProcessor(String query) throws IncorrectQueryException {
    Pattern pattern =
        Pattern.compile(
            "^\\s*CREATE\\s+USER\\s+\\w+\\s+IDENTIFIED\\s+BY\\s+\\w+\\s*;$",
            Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(query);
    if (!matcher.find()) {
      throw new IncorrectQueryException("");
    }

    /* Extract username and password from the query */
    query = query.replace(";", "");
    String[] parts = query.trim().split("\\s+");

    String username = parts[2];
    String password = parts[5];

    /* Create a new User object with the extracted username and password */
    User user = new User(username, password, null);

    /* Add the user to the authentication manager */
    userAuthenticationManager.addUser(user);
    System.out.println("User added successfully!");
  }

  /**
   * Processes a DROP TABLE query by extracting the table name and calls the {@link
   * Database#dropTable(String)} method with the extracted table name.
   *
   * @param query the DROP TABLE query to be processed
   */
  private void dropTableProcessor(String query) {
    query = query.trim();
    String tableName = query.substring(10).trim();
    tableName = tableName.substring(0, tableName.length() - 1).trim();

    if (!tableName.isEmpty()) {
      database.dropTable(tableName);
      return;
    }
    System.out.println("Invalid Syntax");
  }

  /**
   * Processes a DROP USER query by validating the syntax and removing the specified user from the
   * authentication manager.
   *
   * @param query The DROP USER query to be processed.
   */
  private void dropUserProcessor(String query) throws IncorrectQueryException {
    /* Define the pattern for DROP USER query with case insensitivity */
    Pattern pattern = Pattern.compile("^\\s*DROP\\s+USER\\s+\\w+\\s*;$", Pattern.CASE_INSENSITIVE);

    /* Create a matcher for the input query using the defined pattern */
    Matcher matcher = pattern.matcher(query);

    if (!matcher.find()) {
      throw new IncorrectQueryException("");
    }

    /* Extract username from the query */
    String username = query.trim().split("\\s+")[2];
    username = username.replace(";", "");

    userAuthenticationManager.removeUser(username);
  }

  /**
   * Processes an INSERT INTO query by extracting the table name and values, and then calls the
   * {@link Database#insert(String, List)} method with the extracted table name and values.
   *
   * @param query the INSERT INTO query to be processed
   */
  private void insertIntoTableProcessor(String query) {
    query = query.trim();

    String valuesPart = query.substring(11).trim();
    int valuesIndex = valuesPart.toLowerCase().indexOf("values");

    if (valuesIndex != -1) {
      String tableName = valuesPart.substring(0, valuesIndex).trim();

      String valuesString = valuesPart.substring(valuesIndex + 6).trim();
      /* Replace unnecessary characters from the values string*/
      valuesString =
          valuesString.replace("(", "").replace("),", "|").replace(")", "").replace(";", "");

      if (!tableName.isEmpty() && !valuesString.isEmpty()) {
        List<List<String>> valuesList = new ArrayList<>();
        /* Splitting based on | delimiter */
        String[] valuesSets = valuesString.split("\\|");

        for (String set : valuesSets) {
          /* Removing leading and trailing whitespaces */
          set = set.trim();
          set = set.replace("\"", "");
          set = set.replace("'", "");
          /* Splitting individual values */
          String[] valuesArray = set.split(",");
          valuesList.add(Arrays.asList(valuesArray));
        }

        database.insert(tableName, valuesList);
        return;
      }
    }
    System.out.println("Invalid Syntax");
  }

  /**
   * Processes a SELECT query by extracting its details and invoking the corresponding database
   * operation.
   *
   * <p>This method takes a SELECT query as input, extracts relevant details using the {@link
   * #extractSelectQueryDetails(String)} method
   *
   * @param query The SQL SELECT query to be processed.
   * @see #extractSelectQueryDetails(String)
   * @see Database#select(String, List, String)
   */
  private void selectFromTableProcessor(String query) {
    String[] queryBreakdown = extractSelectQueryDetails(query);

    if (queryBreakdown == null || queryBreakdown[0] == null) {
      return;
    }

    String tableName = queryBreakdown[0];
    List<String> columns;
    String condition = null;

    if (queryBreakdown[1] == null) {
      columns = null;
    } else {
      columns = List.of(queryBreakdown[1].split(","));
    }

    if (queryBreakdown[2] != null) {
      condition = queryBreakdown[2];
    }

    database.select(tableName, columns, condition);
  }

  /* Helper Methods */
  private List<String> extractColumns(String[] parts) {
    String[] columnTokens = parts[1].split(",");
    List<String> columns = new ArrayList<>();
    for (int i = 0; i < columnTokens.length; i++) {
      String columnToken = columnTokens[i].trim();
      if (i == columnTokens.length - 1) {
        /* If this is the last column, remove the ") and ;" character */
        columnToken = columnToken.substring(0, columnToken.length() - 2);
      }
      columns.add(columnToken.split(" ")[0]);
    }
    return columns;
  }

  private String[] extractSelectQueryDetails(String query) {
    /* Define an array of regular expression patterns for different SELECT query variations */
    String[] patterns = {
      "^\\s*SELECT\\s+\\*\\s+FROM\\s+(\\w+)\\s*;?\\s*$",
      "^\\s*SELECT\\s+([\\w,\\s]+)\\s+FROM\\s+(\\w+)\\s*;?\\s*$",
      "^\\s*SELECT\\s+\\*\\s+FROM\\s+(\\w+)\\s+WHERE\\s+(.+);?\\s*$",
      "^\\s*SELECT\\s+([\\w,\\s]+)\\s+FROM\\s+(\\w+)\\s+WHERE\\s+(.+);?\\s*$"
    };

    /* Iterate through each pattern to find a match for the input query */
    for (int i = 0; i < patterns.length; i++) {
      /* Compile the regular expression pattern with case-insensitivity */
      Pattern compiledPattern = Pattern.compile(patterns[i], Pattern.CASE_INSENSITIVE);
      /* Create a matcher for the input query using the current pattern */
      Matcher matcher = compiledPattern.matcher(query);

      if (matcher.find()) {
        String tableName;
        String columns;
        String condition;

        if (i == 0) {
          tableName = matcher.group(1);
          columns = null;
          condition = null;
        } else {
          tableName = matcher.group(i == 2 ? 1 : 2);
          columns = matcher.group(i == 1 || i == 3 ? 1 : 2);
          condition = i == 2 || i == 3 ? matcher.group(3) : null;
        }

        if (condition != null) {
          condition = condition.replace(";", "");
        }

        return new String[] {tableName, columns, condition};
      }
    }

    /* Return null if no pattern matches the input query */
    return null;
  }
}
