import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CSVDataSerializerImplementation} class is an implementation of the {@code DataSerializer} interface,
 * providing methods for serializing and deserializing data in CSV format.
 */
public class CustomDelimiterDataSerializerImplementation implements DataSerializer {
  /**
   * @param data the data
   * @return serialized string
   */
  @Override
  public String serializeData(Data data) {
    StringBuilder csvBuilder = new StringBuilder();
    for (List<String> row : data.getData()) {
      /* -_- is my CUSTOM DELIMITER */
      csvBuilder.append(String.join("-_-", row)).append("\n");
    }
    return csvBuilder.toString();
  }

  /**
   * @param reader input reader
   * @param data the data
   */
  @Override
  public void deserializeData(BufferedReader reader, Data data) {
    try {
      List<List<String>> rows = new ArrayList<>();
      String line;
      while ((line = reader.readLine()) != null) {
        rows.add(List.of(line.split("-_-")));
      }
      data.setData(rows);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
