import java.util.List;

/**
 * The {@code Data} class represents a container for storing tabular data consisting of rows and columns.
 */
public class Data {
  private List<List<String>> data;

  public Data(List<List<String>> data) {
    this.data = data;
  }

  public List<List<String>> getData() {
    return data;
  }

  public void setData(List<List<String>> data) {
    this.data = data;
  }

  public void addData(List<List<String>> data) {
    this.data.addAll(data);
  }
}
