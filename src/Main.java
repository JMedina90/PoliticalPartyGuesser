//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/*
Author: Jorge Medina
Class: Computer Science 311 - Artificial Intelligence Assignment 2
Program: Political Party Guesser


This program asks the user a series of questions and assigns "weights" to political parties based on the user's answers. The goal is to guess the user's political party by adding up these weights.

### How it works:
1. **Questions & Weights**:
   - Each answer option is assigned a set of weights (points) for four political parties: Democrat, Republican, Libertarian, and Independent.
   - Example: `{10, 0, 10, 0}` means 10 points for Democrat, 0 for Republican, 10 for Libertarian, and 0 for Independent.

2. **Guessing**:
   - After each question, the program updates the scores and guesses the political party with the highest score.
   - After all questions, the final guess is based on the total scores.

3. **Data Storage**:
   - Responses are saved to a file named after the guessed party (e.g., `Democrat.txt`).

By adding weights dynamically, the program tracks the user's responses and predicts their political party based on cumulative scores.
*/
public class Main {

    public static void main(String[] args) {
        Guesser guess = new Guesser();
        guess.askQuestions();
    }
}
