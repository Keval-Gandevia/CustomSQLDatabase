import java.util.ArrayList;
import java.util.List;

public class Table {
    private String table_name;
    private List<TableAttribute> tableAttributes;
    private List<TableTuple> tableTuples;

    /**
     * constructor
     * @param table_name - name of the table
     * @param tableAttributes - list of the table attributes
     * @param tableTuples - lits of table tuples
     */
    public Table(String table_name, List<TableAttribute> tableAttributes, List<TableTuple> tableTuples) {
        this.table_name = table_name;
        this.tableAttributes = tableAttributes;
        this.tableTuples = tableTuples;
    }

    /**
     *
     * @return - table name
     */
    public String getTableName() {
        return this.table_name;
    }

    /**
     *
     * @return - list of table attributes
     */
    public List<TableAttribute> getTableAttributes() {
        return this.tableAttributes;
    }

    /**
     *
     * @return - list of attributes which contains primary key.
     */
    public List<TableAttribute> getTableAttributesWithPrimaryKey() {
        List<TableAttribute> attributes = new ArrayList<>();
        for(int i = 0; i < this.tableAttributes.size(); i++) {
            if(this.getTableAttributes().get(i).getPrimaryKey().equals("true")) {
                attributes.add(this.getTableAttributes().get(i));
            }
        }
        return attributes;
    }

    /**
     *
     * @return - list of table tuples
     */
    public List<TableTuple> getTableTuples() {
        return this.tableTuples;
    }

    /**
     * method to add single table tuple object to list of table tuples
     * @param tableTuple - table tuple object
     * @return - status of tuple addition
     */
    public boolean addTupleToTupleList(TableTuple tableTuple) {
        this.tableTuples.add(tableTuple);
        return true;
    }
    
}
