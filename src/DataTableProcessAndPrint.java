import java.util.List;

/**
 * The {@code DataTableProcessAndPrint} class provides utility methods for processing and printing tabular data.
 */
public class DataTableProcessAndPrint {
  public void printTable(List<List<String>> data, List<String> columns, String condition) {

    if (data == null || data.isEmpty()) {
      System.out.println("No data to display.");
      return;
    }

    /* Find index of columns in the data */
    int[] columnIndexes = getColumnIndexes(data.getFirst(), columns);

    printHeader(data.getFirst(), columnIndexes);

    /* Print data based on columns and condition */
    printData(data, columnIndexes, condition);
  }

  private int[] getColumnIndexes(List<String> header, List<String> columns) {
    if (columns == null || columns.isEmpty()) {
      /* Return all columns if columns is null or empty */
      int[] indexes = new int[header.size()];
      for (int i = 0; i < header.size(); i++) {
        indexes[i] = i;
      }
      return indexes;
    } else {
      /* Find indexes of specified columns */
      int[] indexes = new int[columns.size()];
      for (int i = 0; i < columns.size(); i++) {
        /* Case-insensitive search */
        indexes[i] = indexOfIgnoreCase(header, columns.get(i));
      }
      return indexes;
    }
  }

  public int indexOfIgnoreCase(List<String> list, String target) {
    for (int i = 0; i < list.size(); i++) {
      /* Getting index ignoring the case */
      if (list.get(i).equalsIgnoreCase(target.trim())) {
        return i;
      }
    }
    return -1;
  }

  private void printHeader(List<String> header, int[] columnIndexes) {
    for (int index : columnIndexes) {
      System.out.print("| " + header.get(index) + "\t");
    }
    System.out.println("|");
    printDash(header.size());
  }

  private void printDash(int columnCount) {
    for (int i = 0; i < columnCount; i++) {
      System.out.print("----------------");
    }
    System.out.println();
  }

  private void printData(List<List<String>> data, int[] columnIndexes, String condition) {
    for (int i = 1; i < data.size(); i++) {
      List<String> row = data.get(i);
      /* Checking the where condition */
      if (meetsCondition(row, data.getFirst(), condition)) {
        /* Print the row data for the selected columns */
        for (int index : columnIndexes) {
          System.out.print("| " + row.get(index) + "\t");
        }
        System.out.println("|");
      }
    }
  }

  private boolean meetsCondition(List<String> row, List<String> header, String condition) {
    if (condition == null || condition.trim().isEmpty()) {
      return true; /* No condition specified, always true*/
    }

    /* Split the condition into parts (e.g., "age > 20" => ["age", ">", "20"]) */
    String[] conditionParts = condition.split("\\s+");

    /* Ensure the condition has the correct number of parts */
    if (conditionParts.length != 3) {
      System.out.println("Invalid condition format: " + condition);
      return false;
    }

    /* Extract the column name, operator, and value from the condition */
    String columnName = conditionParts[0].toLowerCase();
    String operator = conditionParts[1];
    String value = conditionParts[2];

    /* Find the index of the specified column */
    int columnIndex = indexOfIgnoreCase(header, columnName);

    /* Check if the specified column exists in the row */
    if (columnIndex == -1) {
      System.out.println("Column not found: " + columnName);
      return false;
    }

    String columnValue = row.get(columnIndex);

    columnValue = columnValue.trim();
    value = value.trim();

    /* Perform the comparison based on the operator */
    switch (operator) {
      case "<" -> {
        return compareValues(columnValue, value) < 0;
      }
      case ">" -> {
        return compareValues(columnValue, value) > 0;
      }
      case "=" -> {
        return compareValues(columnValue, value) == 0;
      }
      default -> {
        System.out.println("Invalid operator: " + operator);
        return false;
      }
    }
  }

  private int compareValues(String value1, String value2) {
    /* Compare values for both strings and integers */
    try {
      int intValue1 = Integer.parseInt(value1);
      int intValue2 = Integer.parseInt(value2);
      return Integer.compare(intValue1, intValue2);
    } catch (NumberFormatException e) {
      /* If parsing as integers fails, compare as strings */
      return value1.compareTo(value2);
    }
  }
}
