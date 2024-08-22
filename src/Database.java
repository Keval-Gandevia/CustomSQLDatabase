import java.util.ArrayList;
import java.util.List;

public class Database {
    private String database_name;
    private List<Table> tables;

    /**
     * @param dbname     - name of the database
     * @param table_list - List of tables
     */
    public Database(String dbname, List<Table> table_list) {
        database_name = dbname;
        tables = table_list;
    }

    /**
     * @return - name of the database
     */
    public String getDatabase_name() {
        return this.database_name;
    }

    /**
     * @param dbName - name of the database
     * @return - status
     */
    public boolean setDatabase_name(String dbName) {
        this.database_name = dbName;
        return true;
    }

    /**
     * @param table - add table to existing table list.
     * @return - status
     */
    public boolean addTables(Table table) {
        this.tables.add(table);
        return true;
    }

    /**
     * @param tables - set list of the tables
     */
    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    /**
     * @return - list of tables
     */
    public List<Table> getTables() {
        return this.tables;
    }

    /**
     * method to create database
     *
     * @param auth  - user authentication object
     * @param query - user query
     * @return - status of creating database
     */
    public boolean createDatabase(Authentication auth, String[] query) {

        // check for correct query length
        if (query.length != 3) {
            System.out.println("Invalid query!!!");
            return false;
        }
        // check whether database exists or not
        // if it exists then return false as there can't be 2 databases with same name under one username
        boolean databasFileExist = Support.isDatabaseFileExist(auth.getEmail(), query[2]);
        if (!databasFileExist) {
            // create new database
            boolean isSaved = Support.saveDatabaseFile(auth.getEmail(), query[2]);
            if (isSaved) {
                System.out.println("Database created successfully.");
                this.database_name = query[2];
                this.tables = new ArrayList<>();
                return true;
            } else {
                System.out.println("Database creation failed.");
                return false;
            }
        } else {
            System.out.println("Database already exists.");
            return false;
        }
    }

    /**
     * method to drop the database
     *
     * @param auth  - user authentication object
     * @param query - user query
     * @return - status of creating database
     */
    public boolean dropDatabase(Authentication auth, String[] query) {

        // check for correct query length
        if (query.length != 3) {
            System.out.println("Invalid query!!!");
            return false;
        }

        // check whether database exists or not
        boolean databasFileExist = Support.isDatabaseFileExist(auth.getEmail(), query[2]);
        if (databasFileExist) {
            boolean isDeleteDatabase = Support.deleteDatabaseFile(auth.getEmail(), query[2]);
            if (isDeleteDatabase) {
                System.out.println("Database dropped successfully.");
                return true;
            } else {
                System.out.println("Databade deletion failed.");
                return false;
            }
        } else {
            System.out.println("Database does not exist.");
            return false;
        }

    }

    /**
     * method to use database in the program
     *
     * @param auth  - user authentication object
     * @param query - user query
     * @return - database object
     */
    public Database useDatabase(Authentication auth, String[] query) {
        if (query.length != 2) {
            System.out.println("Invalid query!!!");
            return null;
        }
        boolean databaseFileExist = Support.isDatabaseFileExist(auth.getEmail(), query[1]);
        if (databaseFileExist) {
            System.out.println("Database changed to: " + query[1]);
            return Support.prepareDatabaseObject(auth.getEmail(), query[1], this);
        } else {
            System.out.println("Database does not exist.");
            return null;
        }
    }
}
