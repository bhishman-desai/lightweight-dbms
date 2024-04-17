import java.util.List;

/**
 * The {@code DataDefinition} interface defines methods for creating and dropping tables in a dbms.
 */
public interface DataDefinition {
  /**
   * Creates a new table with the specified name and columns.
   *
   * @param tableName The name of the table to be created.
   * @param columns   The list of column names for the new table.
   */
  void createTable(String tableName, List<String> columns);

  /**
   * Drops an existing table with the specified name.
   *
   * @param tableName The name of the table to be dropped.
   */
  void dropTable(String tableName);
}
