/*
1. Create a class to represent the ATM machine.
2. Design the user interface for the ATM, including options such as withdrawing, depositing, and checking the balance.
3. Implement methods for each option, such as withdraw(amount), deposit(amount), and checkBalance().
4. Create a class to represent the user's bank account, which stores the account balance.
5. Connect the ATM class with the user's bank account class to access and modify the account balance.
6. Validate user input to ensure it is within acceptable limits (e.g., sufficient balance for withdrawals).
7. Display appropriate messages to the user based on their chosen options and the success or failure of their transactions.
*/

public class AtmMachine {
    public class AtmInterface {
        public int amount;
        public int balance = 1000;
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
        
        public int checkBalance(){
            return balance;
        }
    }

}
