
public class TableAttribute {
    private String attr_name;
    private String attr_type;

    private String isPrimaryKey;

    /**
     * constructor
     * @param attr_name - name of the attribute
     * @param attr_type - type of the attribute
     */
    public TableAttribute(String attr_name, String attr_type) {
        this.attr_name = attr_name;
        this.attr_type = attr_type;
        this.isPrimaryKey = "false";
    }

    /**
     *
     * @param attr_name - name of the attribute
     * @param attr_type - type of the attribute
     * @param isPrimaryKey - attribute is primary key or not
     */
    public TableAttribute(String attr_name, String attr_type, String isPrimaryKey) {
        this.attr_name = attr_name;
        this.attr_type = attr_type;
        this.isPrimaryKey = isPrimaryKey;
    }

    /**
     *
     * @return - name of the attribute
     */
    public String getAttr_name() {
        return this.attr_name;
    }

    /**
     *
     * @return - type of the attribute
     */
    public String getAttr_type() {
        return this.attr_type;
    }

    /**
     *
     * @return - primary key
     */
    public String getPrimaryKey() {
        return this.isPrimaryKey;
    }

    /**
     * method to check attribute whether it is int or varchar.
     * @return - status of checking attribute's validation
     */
    public boolean checkAttributeType() {

        if(attr_type.equals("int")) {
            return true;
        }
        else if(attr_type.contains("varchar")) {
            try {
                String subString = attr_type.substring(8, attr_type.length() - 1);
                int number = Integer.parseInt(subString);
                return true;
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid query!!!");
                return false;
            }
        }
        System.out.println("Invalid query!!!");
        return false;
    }
}
