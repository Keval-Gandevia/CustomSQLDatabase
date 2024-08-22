import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HandleOperation {

    private Authentication auth;

    /**
     * constructor
     * @param user_auth - user authentication object
     */
    public HandleOperation(Authentication user_auth) {
        auth = user_auth;
    }

    /**
     * method to handle each query of different type
     * @param user_query - query that user has entered to process.
     * @param db - current database object
     * @return - status of the query execution
     */
    public boolean handleQuery(String user_query, Database db) {
        // remove the semicolon from the query
        user_query = user_query.substring(0, user_query.length() - 1);

        // create the object of the Query class
        IQuery q = new Query(user_query);

        String[] strs = q.parseQuery();

        // create database
        if (Support.getQueryType(strs).equals(QueryOption.CREATE_DATABASE)) {
            db.createDatabase(auth, strs);
        }
        // drop database
        else if (Support.getQueryType(strs).equals(QueryOption.DROP_DATABASE)) {
            db.dropDatabase(auth, strs);
        }
        // use database
        else if (Support.getQueryType(strs).equals(QueryOption.USE_DATABASE)) {
            db.useDatabase(auth, strs);
        }
        // create table
        else if (Support.getQueryType(strs).equals(QueryOption.CREATE_TABLE)) {
            boolean isCreated = q.createTable(db);
            if (isCreated) {
                // update the database file
                Support.writeDatabaseToFile(auth.getEmail(), db.getDatabase_name(), db);
                System.out.println("Table is created");
            }
        }
        // select data from table
        else if (Support.getQueryType(strs).equals(QueryOption.SELECT_FROM_TABLE)) {
            boolean isSelectSuccess = q.selectFromTable(db);
        }
        // insert data into table
        else if (Support.getQueryType(strs).equals(QueryOption.INSERT_INTO_TABLE)) {
            boolean isDataAdded = q.insertDataIntoTable(db);
            if (isDataAdded) {
                // update the database file
                Support.writeDatabaseToFile(auth.getEmail(), db.getDatabase_name(), db);
                System.out.println("Data is inserted.");
            }
        } else if (Support.getQueryType(strs).equals(QueryOption.BEGIN_TRANSACTION)) {
            System.out.println("You are entered in a transaction.");
            handleTransaction(db);

        } else if (Support.getQueryType(strs).equals(QueryOption.NO_OPERATION)) {
            System.out.println("Invalid query!!!");
            return false;
        }
        return true;
    }

    /**
     *
     * @param database - current database object
     * @return - status of the transaction
     */
    public boolean handleTransaction(Database database) {

        // list of user queries
        List<String> userQueries = new ArrayList<>();

        // list of queries that we need to perform to persistent data
        List<String> finalQueries = new ArrayList<>();

        String[] strs = new String[]{"no", "operation"};
        Scanner sc = new Scanner(System.in);
        String query = "";

        // exit when end of the transaction encountered.
        while (!(Support.getQueryType(strs).equals(QueryOption.END_TRANSACTION))) {
            System.out.print(auth.getEmail() + "@Transaction-Query> ");
            query = sc.nextLine();
            if (query.charAt(query.length() - 1) != ';') {
                System.out.println("Invalid query!!!");
                continue;
            }

            String tempQuery = query.substring(0, query.length() - 1);
            tempQuery = tempQuery.toLowerCase();
            strs = tempQuery.split(" ");

            // add user query to temporary list
            userQueries.add(query);

            // if user entered commit then we need to make a copy to final query list
            if (Support.getQueryType(strs).equals(QueryOption.COMMIT)) {
                finalQueries.addAll(userQueries);
                userQueries.clear();
            }
            // if user entered rollback then clear the list as we do not need to perform those queries to permanent database.
            else if (Support.getQueryType(strs).equals(QueryOption.ROLLBACK)) {
                userQueries.clear();
            }
        }

        // process all queries
        for (int i = 0; i < finalQueries.size(); i++) {
            handleQuery(finalQueries.get(i), database);
        }
        return true;
    }
}
