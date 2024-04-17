import java.io.BufferedReader;

/**
 * The {@code DataSerializer} interface defines methods for serializing and deserializing data.
 */
public interface DataSerializer {
  String serializeData(Data data);

  void deserializeData(BufferedReader reader, Data data);
}
