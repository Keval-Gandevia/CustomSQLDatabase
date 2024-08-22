import java.util.ArrayList;
import java.util.List;

public class Query implements IQuery {

    private String query = "";

    /**
     * @param user_query - entered user query
     */
    public Query(String user_query) {
        this.query = user_query.toLowerCase();
    }

    /**
     * method that split the query by whitespace.
     *
     * @return - array of strings which contain the query
     */
    public String[] parseQuery() {
        return query.split(" ");
    }

    // method to create table

    /**
     * method to create table
     *
     * @param db - current database object
     * @return - status of table creation
     */
    public boolean createTable(Database db) {

        // if database does not exist.
        if (db.getDatabase_name() == null) {
            System.out.println("Database does not exist.");
            return false;
        }

        String[] strs = parseQuery();

        // check the length of the string
        if (strs.length <= 3) {
            System.out.println("Invalid query!!!");
            return false;
        }

        // fetch the table name
        String table_name = strs[2];

        // check the next token must contains '('
        if (!strs[3].contains("(")) {
            System.out.println("Invalid query!!!");
            return false;
        }

        // check whether entered table name already exists or not.
        for (int i = 0; i < db.getTables().size(); i++) {
            if (table_name.equals(db.getTables().get(i).getTableName())) {
                System.out.println("Table already exists.");
                return false;
            }
        }

        // remove starting and ending bracket;
        String withBracket = query.substring(query.indexOf('(') + 1, query.length() - 1);

        // separate by comma, will get array of attributes
        String[] getAttributes = withBracket.split(",");

        // remove extra white space in all attributes
        for (int i = 0; i < getAttributes.length; i++) {
            getAttributes[i] = getAttributes[i].trim();
        }

        // split by whitespace to get attribute name and its type
        List<TableAttribute> tableAttributeList = new ArrayList<>();
        for (String g : getAttributes) {
            String[] oneAttribute = g.split(" ");
            // create table attribute based on input datatype and constraint.
            // if query contains the primary key, else query does not contain the primary key.
            if (oneAttribute.length == 4) {
                // first check whether the keyword is primary key or not
                if (!(oneAttribute[2].equals("primary") && oneAttribute[3].equals("key"))) {
                    System.out.println("Invalid query!!!");
                    return false;
                }
                TableAttribute tableAttribute = new TableAttribute(oneAttribute[0], oneAttribute[1], "true");
                tableAttributeList.add(tableAttribute);

            } else if (oneAttribute.length == 2) {
                TableAttribute tableAttribute = new TableAttribute(oneAttribute[0], oneAttribute[1]);
                tableAttributeList.add(tableAttribute);
            } else {
                System.out.println("Invalid query!!!");
                return false;
            }
        }

        // allowing only int and varchar datatype
        for (int i = 0; i < tableAttributeList.size(); i++) {
            if (!tableAttributeList.get(i).checkAttributeType()) {
                return false;
            }
        }

        // add tables to current database object.
        db.addTables(new Table(table_name, tableAttributeList, new ArrayList<>()));
        return true;
    }

    /**
     * method to return data from the table
     * @param db    - database object
     * @return - status of selecting data
     */
    public boolean selectFromTable(Database db) {

        // check database exists or not.
        if (db.getDatabase_name() == null) {
            System.out.println("Select database first.");
            return false;
        }

        String[] strs = parseQuery();

        // allowing only select * from table
        if (strs.length != 4) {
            System.out.println("Invalid query!!!");
            return false;
        }

        // fecth the table name
        String tableName = strs[3];

        // get the list of tables
        List<Table> tableList = db.getTables();
        int index = -1;
        for (int i = 0; i < tableList.size(); i++) {
            if (tableList.get(i).getTableName().equals(tableName)) {
                index = i;
            }
        }
        // if table founds
        if (index != -1) {
            List<TableAttribute> tableAttributes = tableList.get(index).getTableAttributes();
            List<TableTuple> tableTuples = tableList.get(index).getTableTuples();
            Support.printTableToScreen(tableAttributes, tableTuples);
        } else {
            System.out.println("Table does not exist.");
            return false;
        }
        return true;
    }

    /**
     * method to insert data into table
     *
     * @param db - database object
     * @return - status of data insertion
     */
    public boolean insertDataIntoTable(Database db) {

        try {
            // check database exists or not.
            if (db.getDatabase_name() == null) {
                System.out.println("Database does not exist.");
                return false;
            }

            String[] strs = parseQuery();

            // fetch the name of the table
            String tableName = strs[2];

            // check given table exists in the database or not.
            List<Table> tableList = db.getTables();
            int tableIndex = -1;
            for (int i = 0; i < tableList.size(); i++) {
                if (tableList.get(i).getTableName().equals(tableName)) {
                    tableIndex = i;
                    break;
                }
            }
            if (tableIndex == -1) {
                System.out.println("Table does not exist.");
                return false;
            }

            // remove starting and ending bracket;
            String withBracket = query.substring(query.indexOf('(') + 1, query.indexOf(')'));

            // now separate by comma, will get array of attributes
            String[] getAttributes = withBracket.split(",");

            // get the attributes of the table from database
            List<TableAttribute> tableAttributes = tableList.get(tableIndex).getTableAttributes();

            // remove extra white space in all attributes
            // check whether the attribute names are same or not as per the original table description.
            for (int i = 0; i < getAttributes.length; i++) {
                getAttributes[i] = getAttributes[i].trim();
                if (!tableAttributes.get(i).getAttr_name().equals(getAttributes[i])) {
                    System.out.println("Table attributes are not matching.");
                    return false;
                }
            }

            String removeValuesKeyword = query.substring(query.indexOf("values") + 6).trim();

            String removeBracket = removeValuesKeyword.substring(removeValuesKeyword.indexOf('(') + 1, removeValuesKeyword.indexOf(')'));

            String[] getValues = removeBracket.split(",");

            // check whether number of values are same as number of attribute provided.
            if (getValues.length != getAttributes.length) {
                System.out.println("Number of attributes are not matching with number of values.");
                return false;
            }
            // remove the extra whitespace
            for (int i = 0; i < getValues.length; i++) {
                getValues[i] = getValues[i].trim();
            }

            for (int i = 0; i < getValues.length; i++) {

                String attributeDatatype = tableList.get(tableIndex).getTableAttributes().get(i).getAttr_type();

                String value = getValues[i];

                // match the attribute datatype with its values
                if (attributeDatatype.equals("int")) {
                    int val = Integer.parseInt(value);
                }
                if (attributeDatatype.contains("varchar")) {
                    String getVarcharLength = attributeDatatype.substring(attributeDatatype.indexOf('(') + 1, attributeDatatype.indexOf(')'));
                    int intLength = Integer.parseInt(getVarcharLength);
                    if (!((value.charAt(0) == '\"') && (value.charAt(value.length() - 1) == '\"') && (value.length() - 2 <= intLength))) {
                        System.out.println("Type mismatched.");
                        return false;
                    }
                    getValues[i] = value.substring(value.indexOf("\"") + 1, value.length() - 1);
                }
            }


            // fetch the table attributes which contains primary key
            List<TableAttribute> attributesWithPrimaryKey = tableList.get(tableIndex).getTableAttributesWithPrimaryKey();

            // Fetch all the tuples and verify if values are getting repeated with new tuple if any of the field contains the primary key
            List<TableTuple> existingTuples = tableList.get(tableIndex).getTableTuples();

            TableTuple tableTuple = new TableTuple();
            for (int i = 0; i < getAttributes.length; i++) {
                for (int j = 0; j < attributesWithPrimaryKey.size(); j++) {
                    if (getAttributes[i].equals(attributesWithPrimaryKey.get(j).getAttr_name())) {
                        for (int k = 0; k < existingTuples.size(); k++) {
                            if (getValues[i].equals(existingTuples.get(k).getTuple().get(getAttributes[i]))) {
                                System.out.println("Primary key constraint violated!!");
                                return false;
                            }
                        }
                    }
                }
                // add tuple
                tableTuple.addTuple(getAttributes[i], getValues[i]);
            }

            //  add tuple to existing list of the tuples for entered table
            tableList.get(tableIndex).addTupleToTupleList(tableTuple);
            return true;

        } catch (Exception e) {
            System.out.println("Invalid query!!");
            return false;
        }
    }

}
