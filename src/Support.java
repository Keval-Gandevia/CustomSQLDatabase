import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Support {

    /**
     * @param query - user query entered on the terminal
     * @return - return the type of the query
     */
    public static QueryOption getQueryType(String[] query) {
        // check for create database
        if (query[0].equals("create") && query[1].equals("database")) {
            return QueryOption.CREATE_DATABASE;
        }
        // check for drop database
        else if (query[0].equals("drop") && query[1].equals("database")) {
            return QueryOption.DROP_DATABASE;
        }
        // check for use database
        else if (query[0].equals("use")) {
            return QueryOption.USE_DATABASE;
        }
        // check for create table
        else if (query[0].equals("create") && query[1].equals("table")) {
            return QueryOption.CREATE_TABLE;
        }
        // check for insert into table
        else if (query[0].equals("insert") && query[1].equals("into")) {
            return QueryOption.INSERT_INTO_TABLE;
        }
        // check for select data from table
        else if (query[0].equals("select") && query[2].equals("from")) {
            return QueryOption.SELECT_FROM_TABLE;
        }
        // check for begin transaction
        else if (query[0].equals("begin") && query[1].equals("transaction")) {
            return QueryOption.BEGIN_TRANSACTION;
        }
        // check for end transaction
        else if (query[0].equals("end") && query[1].equals("transaction")) {
            return QueryOption.END_TRANSACTION;
        }
        // check for commit
        else if (query[0].equals("commit")) {
            return QueryOption.COMMIT;
        }
        // check for rollback
        else if (query[0].equals("rollback")) {
            return QueryOption.ROLLBACK;
        }
        // otherwise return no operation
        return QueryOption.NO_OPERATION;
    }

    /**
     * method to check if database exist or not
     *
     * @param email  - user email
     * @param dbname
     * @return
     */
    public static boolean isDatabaseFileExist(String email, String dbname) {
        try {
            File database_file = new File(FilePath.getPath() + "/" + email + "_" + dbname + FilePath.getFileType());
            if (database_file.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * method to save database to database file.
     *
     * @param email  - user email
     * @param dbname - name of the database entered by the user
     * @return - status of the saving file
     */
    public static boolean saveDatabaseFile(String email, String dbname) {

        try {
            // create a new file for database
            File database_file = new File(FilePath.getPath() + "/" + email + "_" + dbname + FilePath.getFileType());
            database_file.createNewFile();

            PrintWriter printWriter = new PrintWriter(database_file);
            printWriter.write("database_name:" + dbname + "\n");
            printWriter.write("tables_list:[]");
            printWriter.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * method to delete database file
     *
     * @param email  - user email
     * @param dbname - name of the database
     * @return - status of file deletion
     */
    public static boolean deleteDatabaseFile(String email, String dbname) {
        try {
            File database_file = new File(FilePath.getPath() + "/" + email + "_" + dbname + FilePath.getFileType());

            if (database_file.exists()) {
                // delete the database file
                database_file.delete();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // prepare database from database_file

    /**
     * method which read from the database file and create a database object with all its details
     *
     * @param email         - user emal
     * @param database_name - name of the database
     * @param db            - database object
     * @return - created database object
     */
    public static Database prepareDatabaseObject(String email, String database_name, Database db) {
        try {
            // check if the database file exists or not.
            if (isDatabaseFileExist(email, database_name)) {
                File database_file = new File(FilePath.getPath() + "/" + email + "_" + database_name + FilePath.getFileType());

                BufferedReader bufferedReader = new BufferedReader(new FileReader(database_file));
                String line = "";
                String dbName = "";
                String[] tablelist;
                List<Table> database_tables = new ArrayList<>();
                Map<String, List<TableAttribute>> tableMap = new HashMap<>();
                Map<String, List<TableTuple>> tupleMap = new HashMap<>();
                while (line != null) {
                    line = bufferedReader.readLine();

                    if (line != null) {
                        // read database name
                        if (line.startsWith("database_name:")) {
                            dbName = line.substring(14, line.length());
                        }
                        // read the list of tables
                        if (line.startsWith("tables_list:[")) {
                            String tableString = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
                            tablelist = tableString.split(",");

                        }
                        // read the table's attributes
                        if (line.contains("attributes")) {
                            String fetchTableName = line.substring(0, line.indexOf('_'));
                            String removeSquareBracket = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
                            String[] removeComma = removeSquareBracket.split(",");
                            List<TableAttribute> listOfAttributes = new ArrayList<>();
                            for (String s : removeComma) {
                                String removeCurlyBracket = s.substring(1, s.length() - 1);
                                String[] attribute = removeCurlyBracket.split(":");
                                TableAttribute tableAttribute = new TableAttribute(attribute[0], attribute[1], attribute[2]);
                                listOfAttributes.add(tableAttribute);
                            }
                            tableMap.put(fetchTableName, listOfAttributes);
                        }
                        // read the table's tuples
                        if (line.contains("tuples")) {
                            String fetchTableName = line.substring(0, line.indexOf('_'));
                            String removeSquareBracket = line.substring(line.indexOf('[') + 1, line.indexOf(']'));

                            List<TableTuple> listOfTuples = new ArrayList<>();
                            if (!removeSquareBracket.isEmpty()) {
                                String[] removeHyphen = removeSquareBracket.split("-");

                                for (String r : removeHyphen) {
                                    String removeCurlyBracket = r.substring(1, r.length() - 1);
                                    String[] removeComma = removeCurlyBracket.split(",");
                                    TableTuple tuple = new TableTuple();
                                    for (String a : removeComma) {
                                        String[] keyValue = a.split(":");
                                        tuple.addTuple(keyValue[0], keyValue[1]);
                                    }
                                    listOfTuples.add(tuple);
                                }
                            }
                            tupleMap.put(fetchTableName, listOfTuples);
                        }
                    }
                }
                for (Map.Entry<String, List<TableAttribute>> entry : tableMap.entrySet()) {
                    List<TableTuple> tempTuple = tupleMap.get(entry.getKey());
                    database_tables.add(new Table(entry.getKey(), entry.getValue(), tempTuple));
                }

                // set the name of the database
                db.setDatabase_name(dbName);
                // set the table details with its attributes and tuples
                db.setTables(database_tables);

                bufferedReader.close();
                return db;

            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * method to write database object to respective database_file
     *
     * @param email         - user email
     * @param database_name - name of the database
     * @param database      - database object
     * @return - status of write operation
     */
    public static boolean writeDatabaseToFile(String email, String database_name, Database database) {

        try {
            // check if the database file exists or not.
            if (isDatabaseFileExist(email, database_name)) {
                File database_file = new File(FilePath.getPath() + "/" + email + "_" + database_name + FilePath.getFileType());

                StringBuilder stringBuilder = new StringBuilder();

                // first add database name
                stringBuilder.append("database_name:");
                stringBuilder.append(database_name);
                stringBuilder.append("\n");


                // write all tables in the list;
                stringBuilder.append("tables_list:[");
                List<Table> tableList = database.getTables();

                for (int i = 0; i < tableList.size(); i++) {
                    stringBuilder.append(tableList.get(i).getTableName());
                    if (i != tableList.size() - 1) {
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.append("]\n");

                // write all table attributes
                for (int i = 0; i < tableList.size(); i++) {
                    stringBuilder.append(tableList.get(i).getTableName());
                    stringBuilder.append("_attributes:[");
                    List<TableAttribute> tableAttributes = tableList.get(i).getTableAttributes();
                    for (int j = 0; j < tableAttributes.size(); j++) {
                        stringBuilder.append("{");
                        stringBuilder.append(tableAttributes.get(j).getAttr_name());
                        stringBuilder.append(":");
                        stringBuilder.append(tableAttributes.get(j).getAttr_type());
                        stringBuilder.append(":");
                        stringBuilder.append(tableAttributes.get(j).getPrimaryKey());
                        stringBuilder.append("}");

                        if (j != tableAttributes.size() - 1) {
                            stringBuilder.append(",");
                        }
                    }
                    stringBuilder.append("]\n");
                }

                // write all tuples
                for (int i = 0; i < tableList.size(); i++) {
                    stringBuilder.append(tableList.get(i).getTableName());
                    stringBuilder.append("_tuples:[");
                    List<TableTuple> tableTuples = tableList.get(i).getTableTuples();

                    for (int j = 0; j < tableTuples.size(); j++) {
                        stringBuilder.append("{");
                        int mapSize = tableTuples.get(j).getTuple().size();
                        for (Map.Entry<String, String> entry : tableTuples.get(j).getTuple().entrySet()) {
                            stringBuilder.append(entry.getKey());
                            stringBuilder.append(":");
                            stringBuilder.append(entry.getValue());
                            mapSize = mapSize - 1;
                            // skip the comma for last tuple
                            if (mapSize != 0) {
                                stringBuilder.append(",");
                            }
                        }
                        stringBuilder.append("}");
                        if (j != tableTuples.size() - 1) {
                            stringBuilder.append("-");
                        }
                    }
                    stringBuilder.append("]");
                    stringBuilder.append("\n");
                }

                // write database details to file.
                PrintWriter printWriter = new PrintWriter(database_file);
                printWriter.write(stringBuilder.toString());
                printWriter.close();
            } else {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param tableAttributes - list of table attributes
     * @param tableTuples     - list of table tuples
     * @return - status of print operation
     */
    public static boolean printTableToScreen(List<TableAttribute> tableAttributes, List<TableTuple> tableTuples) {

        System.out.println("----------------------------------------");

        for (TableAttribute tableAttribute : tableAttributes) {
            System.out.printf("| %-10s ", tableAttribute.getAttr_name());
        }
        System.out.print("|");
        System.out.println();
        System.out.println("----------------------------------------");

        for (TableTuple tableTuple : tableTuples) {
            Map<String, String> tuple = tableTuple.getTuple();
            for (int i = 0; i < tableAttributes.size(); i++) {
                System.out.printf("| %-10s ", tuple.get(tableAttributes.get(i).getAttr_name()));
            }
            System.out.print("|");
            System.out.println();
            System.out.println("----------------------------------------");
        }
        return true;
    }
}
