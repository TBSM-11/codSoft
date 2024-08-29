/*
1. Create a class to represent the ATM machine. #done
2. Design the user interface for the ATM, including options such as withdrawing, depositing, and checking the balance. #done
3. Implement methods for each option, such as withdraw(amount), deposit(amount), and checkBalance(). #done
4. Create a class to represent the user's bank account, which stores the account balance. 
5. Connect the ATM class with the user's bank account class to access and modify the account balance. 
6. Validate user input to ensure it is within acceptable limits (e.g., sufficient balance for withdrawals). #done
7. Display appropriate messages to the user based on their chosen options and the success or failure of their transactions.
*/
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;
public class AtmMachine {
    Scanner s = new Scanner(System.in);
    public class AtmInterface {
        public int amount;
        public int balance = 1000;

//retrieves the balance from the file
        public AtmInterface(){
            try (BufferedReader reader = new BufferedReader(new FileReader("balanceData.txt"))) {
                balance = Integer.parseInt(reader.readLine());
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
//withdraw method
        public int withdraw(int amount) throws Exception{
            try{
                this.amount = amount;
                return balance - amount;
            }
            catch(Exception e){
                if(amount < 0){
                    System.err.println("Invalid amount entered.");
                }
                if(amount > balance){
                    System.err.println("Insufficient balance.");
                }
            }
            return balance;
        }

//deposit method     
        public int deposit(int amount) throws Exception{
            try {
                this.amount = amount;
                return balance += amount;              
            } 
            catch (Exception e) {
                if(amount < 0){
                    System.err.println("Invalid amount entered.");
                }
            }
            return balance;
        }

//check balance method     
        public int checkBalance(){
            return balance;
        }

//stores the balance on a file 
        public void saveBalanceData(){
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("balanceData.txt"))) {
                writer.write(Integer.toString(balance));
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   public class UserAccount {
        public String Credentials(){
//for card number
            System.out.print("Enter your Card details ->");
            String cardNumber = s.next();   

//for account type
            Boolean accountCheck = false;
            String accountType = "default";
            while (!accountCheck) {
                System.out.println("Enter your Account type(Savings[s]/Current[c]) -> ");
                accountType = s.next();

                if(accountType.equalsIgnoreCase("s")){
                    accountType = "Savings";
                    accountCheck = true;
                }
                if(accountType.equalsIgnoreCase("c")){
                    accountType = "Current";
                    accountCheck = true;
                }
                else{
                    System.out.println("Invalid account type.Please enter a valid Account type.");
                }              
            }

//for password
            boolean passCheck = false;
            int wrongPass = 0;
            while(!passCheck && wrongPass < 3)
            try {
                System.out.print("Enter your Password -> ");
                @SuppressWarnings("unused")
                int pin = s.nextInt();
            } 
            catch (NumberFormatException e) {
                System.out.println("Enter a valid Password");
                wrongPass++;
            }

            return "Your details : "+"Card number -> **** **** "+cardNumber.substring((cardNumber.length()) - 4 )+"\n\tAccount type -> "+accountType+"\n";
            
        }
    } 

    public static void main(String[] args) {
       try (Scanner s = new Scanner(System.in)) {
            AtmMachine a = new AtmMachine();
            UserAccount ac = a.new UserAccount();
            AtmInterface atm = a.new AtmInterface();

//to get user info/credentials
            System.out.println("Please enter your credentials -> ");
            String credentials = ac.Credentials();
            System.out.print(credentials);

//to get user input for options to perform until a valid option is entered(throws InputMismatchException)
            System.out.println("1.Deposit\n2.Withdraw\n3.Check Balance");

            boolean transactionSuccession =  false;
            boolean validOption = false;
            
            while (!validOption) {
                try {
                System.out.print("Enter your option : ");
                int option = s.nextInt();
                int amount;
                validOption = true;

                switch (option) {
                    case 1:
//to deposit amount
                        while (!transactionSuccession) {
                            System.out.print("Enter Amount -> ");
                            amount = s.nextInt();
                            try {
                                atm.deposit(amount);
                                atm.saveBalanceData();
                                transactionSuccession = true;
                            } catch (Exception e) {
                                e.getMessage();
                            }
                            break;                         
                        }


                    case 2:
//to withdraw amount
                        while (!transactionSuccession) {
                            System.out.print("Enter Amount -> ");
                            amount = s.nextInt();
                            try {
                                atm.withdraw(amount);
                                transactionSuccession = true;
                                atm.saveBalanceData();
                            } catch (Exception e) {
                                e.getMessage();
                            }                   
                            break;
                        }
                    case 3:
//to check balance
                        System.out.print("Enter Amount -> ");
                        amount = s.nextInt();
                        try {
                            atm.checkBalance();
                        } catch (Exception e) {
                            e.getMessage();
                        }                    
                        break;

                    default:
                        if (transactionSuccession) {
                            System.out.println("Transaction successful.");
                        }
                        else{
                        System.out.println("Invalid option entered.Transaction failed.");
                        }
                        break;
                }
            } 
            catch (InputMismatchException e) {
                System.out.println("Invalid option entered.");
            }
        }

       }
    }
}
