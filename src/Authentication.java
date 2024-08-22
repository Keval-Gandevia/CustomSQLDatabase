import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

public class Authentication {

    private String email;
    private String password;

    /**
     * Usage: constructor of the class
     *
     * @param email    - user email
     * @param password - user password
     */
    public Authentication(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * @return email of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @return - password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @return - status of whether the user is registered or not.
     */
    public boolean registerUser() {
        try {
            // check if email or password contains the null value
            if (Objects.equals(email, "") || Objects.equals(password, "")) {
                System.out.println("Invalid username or password!!!");
                return false;
            }

            // check database directory exists or not.
            // if it does not exist create one.
            File database_dir = new File(FilePath.getPath());
            if (!database_dir.exists()) {
                System.out.println("Directory created");
                database_dir.mkdir();
            }

            // check user file exists or not.
            // if file does not exist, then create one.
            // It also means that user is the first user of the system.
            File user_file = new File(FilePath.getPath() + "/user" + FilePath.getFileType());
            if (!user_file.exists()) {
                user_file.createNewFile();
                PrintWriter printWriter = new PrintWriter(user_file);
                printWriter.write("email:[\"" + email + "\"]\n");
                printWriter.write("password:[\"" + generateHashPassoword(password) + "\"]\n");
                printWriter.close();
            }
            // search in the database, it will result into 2 case
            // 1) user with same email already exists
            // 2) completely new user
            else {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(user_file));
                String line = "";
                String addNewEmailPassword = "";
                String[] listOfEmail = null, listOfPassword = null;
                while (line != null) {
                    line = bufferedReader.readLine();

                    if (line != null) {
                        // get all email
                        if (line.startsWith("email:[")) {
                            // remove "email:" from the line
                            String removedStart = line.substring(6);
                            // remove square brackets
                            String removedSquareBracket = removedStart.substring(1, removedStart.length() - 1);

                            // split the string by comma
                            listOfEmail = removedSquareBracket.split(",");

                            addNewEmailPassword = addNewEmailPassword + "email:[" + removedSquareBracket + ",\"" + email + "\"]\n";
                        }
                        // get all password
                        else if (line.startsWith("password:[")) {
                            // hash the password before adding to database
                            String hashedPassword = generateHashPassoword(password);
                            String removedStart = line.substring(9);
                            // remove square brackets
                            String removedSquareBracket = removedStart.substring(1, removedStart.length() - 1);

                            // split the string by comma
                            listOfPassword = removedSquareBracket.split(",");

                            addNewEmailPassword = addNewEmailPassword + "password:[" + removedSquareBracket + ",\"" + hashedPassword + "\"]";
                        }
                    }
                }

                // check if user already exists or not with same email.
                for (String s : listOfEmail) {
                    if (Objects.equals(email, s.substring(1, s.length() - 1))) {
                        System.out.println("User with same email ID already exists.");
                        return false;
                    }
                }

                PrintWriter printWriter = new PrintWriter(user_file);
                printWriter.write(addNewEmailPassword);
                printWriter.close();
                bufferedReader.close();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @return - status of whether user is able to login or not.
     */
    public boolean login() {
        try {
            // check if email or password contains the null value
            if (Objects.equals(email, "") || Objects.equals(password, "")) {
                System.out.println("Invalid username or password!!!");
                return false;
            }

            // check database directory exists or not.
            // if directory does not exist then it means that user has to first register and then login.
            File database_dir = new File(FilePath.getPath());
            if (!database_dir.exists()) {
                System.out.println("You are not registered. Please register first and then login.");
                return false;
            }

            // check user file exists or not.
            // if file does not exist, it also means that user is the first user of the system.
            File user_file = new File(FilePath.getPath() + "/user" + FilePath.getFileType());
            if (!user_file.exists()) {
                System.out.println("You are not registered. Please register first and then login.");
                return false;
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(user_file));
            String line = "";
            String[] listOfEmail = null, listOfPassword = null;
            while (line != null) {
                line = bufferedReader.readLine();

                if (line != null) {
                    // get all email
                    if (line.startsWith("email:[")) {
                        // remove "email:" from the line
                        String removedStart = line.substring(6);
                        // remove square brackets
                        String removedSquareBracket = removedStart.substring(1, removedStart.length() - 1);

                        // split the string by comma
                        listOfEmail = removedSquareBracket.split(",");
                    }
                    // get all password
                    else if (line.startsWith("password:[")) {

                        // remove "password:" from the line
                        String removedStart = line.substring(9);
                        // remove square brackets
                        String removedSquareBracket = removedStart.substring(1, removedStart.length() - 1);

                        // split the string by comma
                        listOfPassword = removedSquareBracket.split(",");
                    }
                }
            }

            // search for the email
            int foundIndex = -1;
            for (int i = 0; i < listOfEmail.length; i++) {
                if (Objects.equals(email, listOfEmail[i].substring(1, listOfEmail[i].length() - 1))) {
                    foundIndex = i;
                    break;
                }
            }

            // if email founds
            if (foundIndex != -1) {
                // hash the password before match
                String hashedPassword = generateHashPassoword(password);
                // match password
                if (Objects.equals(hashedPassword, listOfPassword[foundIndex].substring(1, listOfPassword[foundIndex].length() - 1))) {
                    System.out.println("You are successfully logged in...");
                    return true;
                } else {
                    System.out.println("Password does not match!!!");
                    return false;
                }
            } else {
                System.out.println("User name with given email ID does not exists!!");
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * @param password - user password which we need to hash
     * @return - hashed password
     */
    public String generateHashPassoword(String password) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytesOfPassword = password.getBytes();
            byte[] digest = md.digest(bytesOfPassword);

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            String hashValue = sb.toString();

            return hashValue;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param n - number of letters in captcha
     * @return - generated captcha
     */
    public String generateCaptcha(int n) {
        // to generate random integers in the range [0-61]
        Random rand = new Random(62);

        String chrs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        String captcha = "";
        while (n-- > 0) {
            int index = (int) (Math.random() * 62);
            captcha += chrs.charAt(index);
        }

        return captcha;
    }

    /**
     * @param captcha      - captcha that program has generated
     * @param user_captcha - captcha that user has entered
     * @return
     */
    public boolean checkCaptcha(String captcha, String user_captcha) {
        return captcha.equals(user_captcha);
    }
}
