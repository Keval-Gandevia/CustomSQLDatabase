import java.util.HashMap;
import java.util.Map;

public class TableTuple {
    private Map<String, String> tuple = new HashMap<>();

    /**
     *
     * @param key - name of the attribute
     * @param value - value of the attribute
     * @return
     */
    public boolean addTuple(String key, String value) {
        this.tuple.put(key, value);
        return true;
    }

    /**
     *
     * @return - tuple
     */
    public Map<String, String> getTuple() {
        return this.tuple;
    }
}
