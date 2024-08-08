/* 
NUMBER GAME
1. Generate a random number within a specified range, such as 1 to 100.
2. Prompt the user to enter their guess for the generated number.
3. Compare the user's guess with the generated number and provide feedback on whether the guess
is correct, too high, or too low.
4. Repeat steps 2 and 3 until the user guesses the correct number.
You can incorporate additional details as follows:
5. Limit the number of attempts the user has to guess the number.
6. Add the option for multiple rounds, allowing the user to play again.
7. Display the user's score, which can be based on the number of attempts taken or rounds won.
*/
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class numberGame {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Random random = new Random();

        int winCounter = 0;
        int roundCounter = 1;
        boolean playRounds = false;

        System.out.print("\t  --NUMBER GAME--\n");

        while (!playRounds) {
// 1. random number generator
            int randomInt = random.nextInt(100) + 1;

// 7. user's score display
            System.out.println("\n\t| Total wins -> " + winCounter + " |");
            System.out.println("\n\t| Round " + roundCounter + " begins! |");

// 5. limiter for no. of attempts
            for (int no_of_attempts = 0; no_of_attempts < 5; no_of_attempts++) {
                System.out.println("\nRemaining " + (5 - no_of_attempts) + " ❤️");

// 2. user prompt to enter their guess
                int guessedNumber = 0;
                boolean validGuess = false;

                while (!validGuess) {
                    System.out.print("Enter your guess -> ");
                    try {
                        guessedNumber = s.nextInt();
                        validGuess = true;
                    } catch (InputMismatchException e) {
                        System.out.println("invalid input. Please enter a number.");
                        s.next();
                    }
                }

// 3. compares the user's guess and provides feedback
                if (guessedNumber == randomInt) {
                    System.out.println("YOU WON!!! " + guessedNumber + " is the correct number.");
                    winCounter++;
                    break;
                }
                 else if (guessedNumber != randomInt) {
                    if (guessedNumber > randomInt) {
                        System.out.println("Guessed number is too high.");
                    }
                    if (guessedNumber < randomInt) {
                        System.out.println("Guessed number is too low.");
                    }
                    if (no_of_attempts == 4) {
                        System.out.println("\nCorrect number is " + randomInt + ".\n\tYOU LOSE!!!");
                        break;
                    }

                }
            }
// 6. option for multiple rounds
            System.out.print("Do you want to play another round?(y/n) -> ");
            boolean validOption = false;

            while (!validOption) {
                String round = s.next();

                if (round.equalsIgnoreCase("y") || round.equalsIgnoreCase("n")) {
                    validOption = true;

                    if(round.equalsIgnoreCase("y")){
                        playRounds = false;
                    }
                    if(round.equalsIgnoreCase("n"))
                        playRounds = true;
                        System.out.println("\n\tThank you for playing.");

                }
                else {
                    System.out.println("Invalid option !\nPlease enter a valid option(y/n)");
                    System.out.print("Do you want to play another round?(y/n) -> ");
                }             
            }

            roundCounter++;

        }

        s.close();

    }
}