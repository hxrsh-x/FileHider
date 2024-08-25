package views;

import dao.UserDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    public void welcomeScreen() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-----JAVA FILE HIDER APP-----");
        System.out.println("Press 1 to LOGIN");
        System.out.println("Press 2 to SIGNUP");
        System.out.println("Press 0 to EXIT");
        int choice = 0;
        try {
            choice = Integer.parseInt(br.readLine());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        switch (choice) {
            case 1 -> login();
            case 2 -> signUp();
            case 0 -> System.exit(0);
        }
    }

    private void login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Email : ");
        String email = sc.nextLine();
        try {
            if(UserDAO.isExists(email)) {
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, genOTP);
                System.out.print("Enter OTP : ");
                String otp = sc.nextLine();
                if(otp.equals(genOTP)) {
                   new UserView(email).home();

                } else {
                    System.out.println("Wrong OTP Entered. Please Enter Correct OTP");
                }
            } else {
                System.out.println("User not found. Please SignUp first.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    private void signUp() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Name : ");
        String name = sc.nextLine();
        System.out.print("Enter Email : ");
        String email = sc.nextLine();
        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, genOTP);
        System.out.print("Enter OTP : ");
        String otp = sc.nextLine();
        if(otp.equals(genOTP)) {
            User user = new User(name, email);
            int response = UserService.saveUser(user);
            switch (response) {
                case 0 -> System.out.println("User Resigtered Successfully");
                case 1 -> System.out.println("User Already Exists, Please Log in");
            }
        } else {
            System.out.println("Wrong OTP");
        }

    }
}
