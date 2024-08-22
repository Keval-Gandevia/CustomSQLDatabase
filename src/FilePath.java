public class FilePath {
    private final static String directoryPath = "database";
    private final static String fileType = ".txt";

    /**
     *
     * @return - path of the 'database' directory
     */
    public static String getPath() {
        return directoryPath;
    }

    /**
     *
     * @return - return the type of the file.
     */
    public static String getFileType() {
        return fileType;
    }
}
