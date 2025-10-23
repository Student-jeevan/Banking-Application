package app;

import service.BankService;
import service.BankServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();
        boolean running  = true;
        System.out.println("Welcome to Console Banking Application");
        while(running){
            System.out.println(
                    """
                    1) Open Account
                    2) Deposit
                    3) withdraw
                    4) Transfer
                    5) Account Statement
                    6) list Accounts
                    7) Search Accounts
                    0) Exit
                    """
            );
            System.out.println("CHOOSE: ");
            String choice  = scanner.nextLine().trim();
            System.out.println("CHOICE: "+choice);
            switch(choice){
                case "1"-> openAccount(scanner , bankService);
                case "2"-> deposit(scanner , bankService);
                case "3"-> withdraw(scanner);
                case "4"-> transfer(scanner);
                case "5"-> statement(scanner);
                case "6"-> listAccounts(scanner , bankService);
                case "7"-> searchAccount(scanner);
                case "0" -> running  = false;

            }
        }

    }

    private static void searchAccount(Scanner scanner) {
    }

    private static void listAccounts(Scanner scanner , BankService bankService) {
        bankService.listAccounts().forEach(a->{
            System.out.println(a.getAccountNumber() +" | "+ a.getAccountType() +" | "+a.getBalance());
        });
    }

    private static void statement(Scanner scanner) {
    }

    private static void transfer(Scanner scanner) {
    }

    private static void withdraw(Scanner scanner) {
    }

    private static void deposit(Scanner scanner , BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber = scanner.nextLine();
        System.out.println("Amount: ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.deposit(accountNumber , amount , "Deposited");
        System.out.println("Deposited");
    }

    private static void openAccount(Scanner scanner , BankService bankService) {
        System.out.println("Customer name: ");
        String name = scanner.nextLine().trim();
        System.out.println("Customer email: ");
        String email = scanner.nextLine().trim();
        System.out.println("Account Type (SAVING/CURRENT): ");
        String type = scanner.nextLine().trim();
        System.out.println("initial deposit (optional , blank for 0): ");
        String amountStr = scanner.nextLine().trim();
        double initial  = Double.parseDouble(amountStr);
        String accountNumber = bankService.openAccount(name , email, type);
        if(initial>0){
            bankService.deposit(accountNumber , initial , "initial deposit");
        }
        System.out.println("Account opened: "+accountNumber);

    }
}
