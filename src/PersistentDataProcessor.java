import java.io.*;

/**
 * The {@code PersistentDataProcessor} class provides methods for loading and saving data to a file using a {@link DataSerializer}.
 */
public class PersistentDataProcessor {
  private final String dataFilePath;
  private final DataSerializer dataSerializer;

  public PersistentDataProcessor(String dataFilePath, DataSerializer dataSerializer) {
    this.dataFilePath = dataFilePath;
    this.dataSerializer = dataSerializer;
  }

  /**
   * Loads data from a file and deserializes it into a Data object.
   *
   * @param data the Data object to store the loaded data
   * @throws IOException if an error occurs while reading from the file
   */
  public void loadData(Data data) {
    try (FileReader fileReader = new FileReader(dataFilePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {

      /* Deserialize data from the read string */
      dataSerializer.deserializeData(bufferedReader, data);

    } catch (IOException e) {
      System.out.println("Error loading data from file: " + dataFilePath);
      e.printStackTrace();
    }
  }

  /**
   * Serializes data from a Data object and saves it to a file.
   *
   * @param data the Data object to get the data from
   */
  public void saveData(Data data) {
    try (FileWriter fileWriter = new FileWriter(dataFilePath);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

      /* Serialize data to a string */
      String serializedData = dataSerializer.serializeData(data);

      /* Write serialized data to the file */
      bufferedWriter.write(serializedData);
    } catch (IOException e) {
      System.out.println("Error saving data to file: " + dataFilePath);
      e.printStackTrace();
    }
  }
}
