public interface IQuery {

    /**
     * method that split the query by whitespace.
     *
     * @return - array of strings which contain the query
     */
    public String[] parseQuery();
    /**
     * method to create table
     *
     * @param db - current database object
     * @return - status of table creation
     */
    public boolean createTable(Database db);

    /**
     * method to return data from the table
     * @param db    - database object
     * @return - status of selecting data
     */
    public boolean selectFromTable(Database db);

    /**
     * method to insert data into table
     *
     * @param db - database object
     * @return - status of data insertion
     */
    public boolean insertDataIntoTable(Database db);

}
