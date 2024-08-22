import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Authentication auth = null;

        while (true) {
            System.out.println("Press key 1 or 2 based on your preference......");
            System.out.println("1. Register");
            System.out.println("2. Login");
            int choice = sc.nextInt();
            sc.nextLine();

            String email, password, captcha, user_captcha;
            boolean isRegister = false, isLogin = false;

            switch (choice) {
                case 1:
                    // register logic
                    System.out.print("Enter your email: ");
                    email = sc.nextLine();
                    System.out.print("Enter your password: ");
                    password = sc.nextLine();
                    auth = new Authentication(email, password);

                    // generate captcha of 6 letters
                    captcha = auth.generateCaptcha(6);
                    System.out.println("Enter given captcha: " + captcha);
                    user_captcha = sc.nextLine();
                    if (!auth.checkCaptcha(captcha, user_captcha)) {
                        System.out.println("Captcha not macthed!!");
                    } else {

                        isRegister = auth.registerUser();
                        if (isRegister) {
                            System.out.println("User is registered.");
                        }
                    }

                    break;
                case 2:
                    // login logic
                    System.out.print("Enter your email: ");
                    email = sc.nextLine();
                    System.out.print("Enter your password: ");
                    password = sc.nextLine();
                    auth = new Authentication(email, password);

                    // generate captcha of 6 letters
                    captcha = auth.generateCaptcha(6);
                    System.out.println("Enter given captcha: " + captcha);
                    user_captcha = sc.nextLine();
                    if (!auth.checkCaptcha(captcha, user_captcha)) {
                        System.out.println("Captcha not macthed!!");
                    } else {
                        isLogin = auth.login();
                    }
                    break;

                default:
                    System.out.println("Incorrect choice!!!. Again choose preference.");
            }
            // if user is registered or logged in then stop this loop
            if (isRegister || isLogin) {
                break;
            }
        }

        // Ask for a query to user.
        System.out.println("***********************");
        Database database = new Database(null, new ArrayList<>());
        HandleOperation handleOperation = new HandleOperation(auth);
        while (true) {
            System.out.print(auth.getEmail() + "@Query> ");
            String user_query = sc.nextLine();

            // check the query ends with semicolon or not.
            if (user_query.charAt(user_query.length() - 1) != ';') {
                System.out.println("Invalid query!!!");
                continue;
            }
            handleOperation.handleQuery(user_query.trim(), database);
        }
    }
}