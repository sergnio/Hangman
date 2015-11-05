import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    // global variable declarations
    static StringBuilder chosenWord = new StringBuilder("none");
    static ArrayList<Character> guessedLetters = new ArrayList<Character>(
            chosenWord.length());
    final static ArrayList<String> words = new ArrayList<String>();
    static StringBuilder underscoredWord = new StringBuilder("");
    static int remainingTries = 5;
    static boolean hasWon = false;
    static int correctGuesses = 0;
    static boolean playAnswer = false;

    // this method chooses a random word from the read-in txt file, and
    public static void createRandomWord() {
        refresh();

        Random random = new Random();
        String randomWord = words.get(random.nextInt(words.size()));
        chosenWord = new StringBuilder(randomWord);

        for (int i = 0; i < chosenWord.length() - 1; i++) {
            underscoredWord.append("_ ");
        }
        underscoredWord.append("_");

    }

    // Resets everything to play a new game
    private static void refresh() {
        underscoredWord = new StringBuilder("");
        remainingTries = 5;
        correctGuesses = 0;
        guessedLetters = new ArrayList<Character>(
                chosenWord.length());
        hasWon = false;
        playAnswer = false;
    }

    // this just checks if you have beat the game by guessing all the correct letters
    public static boolean hasWon() {
        if (correctGuesses == chosenWord.length()) {
            hasWon = true;
        }
        return hasWon;
    }

    // prompts you to play again. enter 'Y' to play again, or 'N' to exit the program.
    private static boolean playAgain() {

        System.out.println("Would you like to play again?");
        System.out.println("Type Y to play again, otherwise type N");

        while (playAnswer == false) {

            Scanner reader = new Scanner(System.in);
            String readNext = reader.next().toUpperCase();
            StringBuffer input = new StringBuffer(readNext);


            // I want to compare the input and see if its a yes or no, and if not then say that its not
            // otherwise if it is a yes or no, reply with true or false accordingly. Maybe read the input as a string builder instead of character?
            if (input.length() > 1) {
                System.out.println("You input too many letters. Please enter a single letter: " + "\n");
                continue;
            }

            // if user inputs 'Y', it "breaks" out of while loop and returns true
            else if (input.charAt(0) == 'Y') {
                System.out.println("You chose: YES");
                playAnswer = true;
            }

            // if user inputs 'N', it breaks out of while loop and returns false
            if (input.charAt(0) == 'N') {
                System.out.println("You chose: NO");
                break;
            }
        }
        return  playAnswer;
    }

    // reads in words from txt files, and puts them in ArrayList separated by spaces.
    // also calls createRandomWord() to create a random word right away.
    public static void readInWords() {
        try {
            @SuppressWarnings("resource")
            BufferedReader txtReader = new BufferedReader(new FileReader("src/Resources/Hangman.txt"));
            String line = txtReader.readLine();

            while (line != null) {
                String[] wordsLine = line.split(" ");
                for (String word : wordsLine) {
                    words.add(word);
                }
                line = txtReader.readLine();
            }
            createRandomWord();
        }

        // catches exceptions and prints them out
        catch (Exception exception) {
            System.err.printf("Exception caught: %s", exception.toString());
            System.exit(0);
        }
    }

    // if you still have remaining guesses and haven't guessed the entire word, this method allows you to
    // input a letter. won't let you input a letter if you've guessed it already. also won't let you input
    // multiple letters, and ignores spaces as inputs.
    public static void guess() {
        while (remainingTries != 0 && hasWon == false) {
            System.out.println("Enter a letter: ");

            Scanner reader = new Scanner(System.in);
            String readNext = reader.next().toUpperCase();
            if (readNext.length() > 1) {
                System.out.println("You input too many letters. Please enter a single letter: " + "\n");
                continue;
            }
            char chosenLetter =  readNext.charAt(0);

            // checks if you haven't guessed this input letter
            if (!guessedLetters.contains(chosenLetter)) {
                guessedLetters.add(chosenLetter);

                // checks if the letter is a match in the word you're trying to guess
                if (isMatch(chosenLetter) == false) {
                    remainingTries--;
                    System.out.println("Sorry, wrong letter! Try again." + "\n");
                    printInfo();
                }
                // if you guess the correct letter, it will just display the normal information without
                else {
                    printInfo();
                }
            }
            else {
                System.out.println("You have already guessed this letter!" + "\n");
                printInfo();
            }
        }
    }

    // checks if the input letter is within the chosen word, and replaces the underscores accordingly.
    private static boolean isMatch(char chosenLetter) {
        int length = underscoredWord.length();
        boolean isMatch = false;

        if (chosenLetter == chosenWord.charAt(0)) {
            underscoredWord.replace(0, 1, Character.toString(chosenLetter));
            isMatch = true;
            correctGuesses++;
        }

        for (int i = 1; i < chosenWord.length(); i++) {
            if (chosenWord.charAt(i) == chosenLetter) {
                underscoredWord.replace(2 * i, (2 * i + 1), Character.toString(chosenWord.charAt(i)));
                isMatch = true;
                correctGuesses++;
            }
        }
        hasWon();
        return isMatch;
    }

    // prints out misc info: how many guesses left, what the word to be guessed looks like.
    // checks if you've won, and also prompts you to play again if you want to play again.
    static public void printInfo() {

        if (remainingTries!=0 && hasWon == true){
            System.out.println(underscoredWord + "\n");
            System.out.println("Congratulations! You won!" + "\n");

            // Just waits one second before before it prompts you to play again
            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            playAgain();
        }

        if (remainingTries != 0 && hasWon == false) {
            System.out.println("You have " + remainingTries + " remaining guessed left.");
            System.out.println(underscoredWord);
            System.out.println("You've guessed these letters " + guessedLetters.toString() + "\n");
        }

        if (remainingTries == 0) {
                System.out.println("Game over!");
                playAgain();
        }
    }

    // main to run the program, and also checks if you want to play again after playing the first time.
    public static void main(String[] args) {
        readInWords();
        printInfo();
        guess();

        while (playAnswer == true) {
            createRandomWord();
            printInfo();
            guess();
        }
        System.out.println("Thanks for playing!");
    }
}
