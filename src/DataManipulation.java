import java.util.List;

/**
 * The {@code DataManipulation} interface defines methods for manipulating data in a dbms.
 */
public interface DataManipulation {
  /**
   * Selects data from a table based on the specified columns and condition.
   *
   * @param tableName The name of the table from which to select data.
   * @param columns   The list of column names to be selected. If null or empty, all columns are selected.
   * @param condition The condition to be applied to the selection. If null or empty, no condition is applied.
   */
  void select(String tableName, List<String> columns, String condition);

  /**
   * Inserts data into a table.
   *
   * @param tableName The name of the table into which data is to be inserted.
   * @param values    The data to be inserted, represented as a List of Lists of Strings.
   */
  void insert(String tableName, List<List<String>> values);
}
